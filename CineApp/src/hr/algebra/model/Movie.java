/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Mihael
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "title")
    private String title;
    @XmlJavaTypeAdapter(PublishedDateAdapter.class)
    @XmlElement(name = "publisheddate")
    private LocalDateTime publishedDate;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "originaltitle")
    private String originalTitle;
    @XmlElementWrapper
    @XmlElement(name = "director")
    private List<Person> directors;
    @XmlElementWrapper
    @XmlElement(name = "actor")
    private List<Person> actors;
    @XmlElement(name = "duration")
    private int duration;
    @XmlElement(name = "genre")
    private String genre;
    @XmlElement(name = "picturepath")
    private String picturePath;
    @XmlElement(name = "link")
    private String link;
    @XmlJavaTypeAdapter(StartDateAdapter.class)
    @XmlElement(name = "startdate")
    private LocalDate startDate;
    
    public Movie() {
    }

    public Movie(String title, LocalDateTime publishedDate, String description, String originalTitle, List<Person> directors, List<Person> actors, int duration, String genre, String picturePath, String link, LocalDate startDate) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.description = description;
        this.originalTitle = originalTitle;
        this.directors = directors;
        this.actors = actors;
        this.duration = duration;
        this.genre = genre;
        this.picturePath = picturePath;
        this.link = link;
        this.startDate = startDate;
    }    
       
    public Movie(int id, String title, LocalDateTime publishedDate, String description, String originalTitle, List<Person> directors, List<Person> actors, int duration, String genre, String picturePath, String link, LocalDate startDate) {
        this(title, publishedDate, description, originalTitle, directors, actors, duration, genre, picturePath, link, startDate);
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public List<Person> getActors() {
        return actors;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getLink() {
        return link;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    

    @Override
    public String toString() {
        return id + " - " + title;
    }
}
