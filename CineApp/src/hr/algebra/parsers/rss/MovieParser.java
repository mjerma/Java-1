/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.parsers.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.enums.TagType;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Mihael
 */
public class MovieParser {
    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    private static final int TIMEOUT = 10000;
    private static final String REQUEST_METHOD = "GET";
    
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";
    
    private static final DateTimeFormatter PUB_DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final DateTimeFormatter START_DATE_FORMATTER =  DateTimeFormatter.ofPattern("d.M.yyyy");


    private static final Random RANDOM = new Random();
    public static List<Movie> parse() throws IOException, XMLStreamException {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL, TIMEOUT, REQUEST_METHOD);
        XMLEventReader reader = ParserFactory.createStaxParser(con.getInputStream());

        Optional<TagType> tagType = Optional.empty();
        Movie movie = null;
        StartElement startElement = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            // trazim kojeg je tipa event (start element) i ak je jedan od onih u tagtype ga spremam i citam
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: // html tagovi item,title,pubdate...
                    startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    tagType = TagType.from(qName);
                    break;
                case XMLStreamConstants.CHARACTERS: // sadrzaj html tagova, podaci
                    if (tagType.isPresent()) {
                        Characters characters = event.asCharacters();
                        String data = characters.getData().trim();
                        
                        switch (tagType.get()) {
                            case ITEM:
                                movie = new Movie();
                                movies.add(movie);
                                break;
                            case TITLE:
                                if(movie != null && !data.isEmpty()){
                                    movie.setTitle(data);
                                }
                                break;
                            case PUB_DATE:
                                if(movie != null && !data.isEmpty()){
                                    LocalDateTime publishedDate = LocalDateTime.parse(data, PUB_DATE_FORMATTER);
                                    movie.setPublishedDate(publishedDate);
                                }
                                break;
                            case DESCRIPTION:
                                if(movie != null && !data.isEmpty()){
                                    String description = (data.substring(data.indexOf(">")+1, data.lastIndexOf("<"))).replaceAll("\\<[^>]*>","");
                                    movie.setDescription(description);
                                }
                                break;
                            case ORIG_TITLE:
                                if(movie != null && !data.isEmpty()){
                                    movie.setOriginalTitle(data);
                                }
                                break;
                            case DIRECTOR:
                                if(movie != null && !data.isEmpty()){
                                    List<Person> directorsList = handlePeople(data);
                                    movie.setDirectors(directorsList);
                                }
                                break;
                            case ACTORS:
                                if(movie != null && !data.isEmpty()){
                                    //Splitam ime i prezime glumca i spremam ih zasebno
                                    List<Person> actorsList = handlePeople(data);
                                    movie.setActors(actorsList);
                                }
                                break;
                            case DURATION:
                                if(movie != null && !data.isEmpty()){
                                    movie.setDuration(Integer.parseInt(data));
                                }
                                break;
                            case GENRE:
                                if(movie != null && !data.isEmpty()){
                                    movie.setGenre(data);
                                }
                                break;
                            case IMG_URL:
                                if(movie != null && !data.isEmpty()){
                                    //pretvori link u https prije handlanja
                                    String picture = data.replace("http", "https");
                                    handlePicture(movie, picture);
                                }
                                break;
                            case LINK:
                                if(movie != null && !data.isEmpty()){
                                    movie.setLink(data);
                                }
                                break;
                            case START_DATE:
                                if(movie != null && !data.isEmpty()){
                                    LocalDate startDate = LocalDate.parse(data, START_DATE_FORMATTER);
                                    movie.setStartDate(startDate);
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        return movies;
    }

    public static List<Person> handlePeople(String data) {
        //Splitam osobe po zarezu saljem helper metodi i spremam u listu
        String[] people = data.split(",");
        List<Person> peopleList = new ArrayList<>();
        for (String person : people) {
            peopleList.add(handlePerson(person));
        }
        return peopleList;
    }

    public static Person handlePerson(String personData){
        //Splitam ime, srednje ime i prezime osobe iz stringa
        String[] details = personData.trim().split(" ");
        if (details.length == 1) {
            return new Person(details[0]);
        }
        if (details.length == 3) {
            return new Person(details[0], details[1], details[2]);
        }
        return new Person(details[0], details[1]);
        
    }
    
    private static void handlePicture(Movie movie, String pictureUrl) throws IOException {
        String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
        if (ext.length() > 4) {
            ext = EXT;
        }
        String pictureName = Math.abs(RANDOM.nextInt()) + ext;
        String localPicturePath = DIR + File.separator + pictureName;

        FileUtils.copyFromUrl(pictureUrl, localPicturePath);
        
        movie.setPicturePath(localPicturePath);
    }
    
}
