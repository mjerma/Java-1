/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import javax.swing.JOptionPane;

/**
 *
 * @author Mihael
 */
public class SqlRepository implements Repository {

    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String IS_ADMINISTRATOR = "IsAdministrator";
    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String DESCRIPTION = "Description";
    private static final String ORIGINAL_TITLE = "OriginalTitle";
    private static final String DURATION = "Duration";
    private static final String GENRE = "Genre";
    private static final String PICTURE_PATH = "PicturePath";
    private static final String LINK = "Link";
    private static final String START_DATE = "StartDate";
    private static final String ID_DIRECTOR = "IDDirector";
    private static final String ID_ACTOR = "IDActor";
    private static final String FIRST_NAME = "FirstName";
    private static final String MIDDLE_NAME = "MiddleName";
    private static final String LAST_NAME = "LastName";

    
    private static final String CREATE_ADMIN_USER = "{ CALL createAdminUser }";
    private static final String CREATE_USER = "{ CALL createUser (?,?,?) }";
    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?,?,?,?) }";
    private static final String CREATE_DIRECTOR = "{ CALL createDirector (?,?,?,?) }";
    private static final String CREATE_MOVIE_DIRECTOR = "{ CALL createMovieDirector (?,?) }";
    private static final String CREATE_ACTOR = "{ CALL createActor (?,?,?,?,?) }";
    private static final String CREATE_MOVIE_ACTOR = "{ CALL createMovieActor (?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String DELETE_ALL_DATA = "{ CALL deleteAllData }";
    private static final String DELETE_MOVIE_PEOPLE = "{ CALL deleteMoviePeople (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIE_DIRECTORS = "{ CALL selectMovieDirectors (?) }";
    private static final String SELECT_MOVIE_ACTORS = "{ CALL selectMovieActors (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";
    private static final String SELECT_ACTORS = "{ CALL selectActors }";
    private static final String SELECT_USER = "{ CALL selectUser (?,?) }";

    @Override
    public void createAdminUser() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ADMIN_USER)) {
            stmt.executeUpdate();
        } 
    }
    
    @Override
    public void createUser(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.BIT);
            stmt.executeUpdate();
            
            if(!stmt.getBoolean(3)){
                JOptionPane.showMessageDialog(null, "Username already exists, choose different one.");
            }
            
            else{
                JOptionPane.showMessageDialog(null, "User created");
            }
        } 
    }
    
    @Override
    public Optional<User> selectUser(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        ResultSet rs;
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_USER)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            rs = stmt.executeQuery();
            if(rs.next()){
               return Optional.of(new User(rs.getString(USERNAME), rs.getString(PASSWORD), rs.getBoolean(IS_ADMINISTRATOR)));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public void createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        CallableStatement stmt;
        int movieID;
        int PersonID;
        try (Connection con = dataSource.getConnection()) {
            
            stmt = con.prepareCall(CREATE_MOVIE);
            
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setString(3, movie.getDescription());
            stmt.setString(4, movie.getOriginalTitle());
            stmt.setInt(5, movie.getDuration());
            stmt.setString(6, movie.getGenre());
            stmt.setString(7, movie.getPicturePath());
            stmt.setString(8, movie.getLink());
            stmt.setString(9, movie.getStartDate().format(DateTimeFormatter.ISO_DATE));
            stmt.registerOutParameter(10, Types.INTEGER);

            stmt.executeUpdate();
            movieID = stmt.getInt(10);
            
            if(movieID != 0){
                
                //Ovdje cepam redatelje 
                for (Person director : movie.getDirectors()) {
                    stmt = con.prepareCall(CREATE_DIRECTOR);
                    
                    stmt.setString(1, director.getFirstName());
                    stmt.setString(2, director.getMiddleName());
                    stmt.setString(3, director.getLastName());
                    stmt.registerOutParameter(4, Types.INTEGER);
                
                    stmt.executeUpdate();
                    PersonID = stmt.getInt(4);
                    
                    //Povezi film i redatelja
                    stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR);
                    
                    stmt.setInt(1, movieID);
                    stmt.setInt(2, PersonID);
                    
                    stmt.executeUpdate();
                }
                
                //Ovdje cepam glumce
                if(movie.getActors() != null){
                    for (Person actor : movie.getActors()) {
                        stmt = con.prepareCall(CREATE_ACTOR);
                    
                        stmt.setString(1, actor.getFirstName());
                        stmt.setString(2, actor.getMiddleName());
                        stmt.setString(3, actor.getLastName());
                        stmt.registerOutParameter(4, Types.INTEGER);
                        stmt.registerOutParameter(5, Types.INTEGER);
                
                        stmt.executeUpdate();
                        PersonID = stmt.getInt(5);
                    
                        //Povezi film i glumca
                        stmt = con.prepareCall(CREATE_MOVIE_ACTOR);
                    
                        stmt.setInt(1, movieID);
                        stmt.setInt(2, PersonID);
                        
                        stmt.executeUpdate();
                    
                    }
                } 
            }
        }
    }
    
    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        for (Movie movie : movies) {
            createMovie(movie);
        }
    }
    
    @Override
    public boolean createActor(Person actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        CallableStatement stmt;
        try (Connection con = dataSource.getConnection()) {
            stmt = con.prepareCall(CREATE_ACTOR);
            
            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getMiddleName());
            stmt.setString(3, actor.getLastName());
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.executeUpdate();
            
            return stmt.getBoolean(4);
            
        }
    }
    
    public void updateMovie(int id, Movie data) throws Exception {
        int PersonID;
        CallableStatement stmt;
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            
            stmt = con.prepareCall(UPDATE_MOVIE);
            
            stmt.setString(1, data.getTitle());
            stmt.setString(2, data.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setString(3, data.getDescription());
            stmt.setString(4, data.getOriginalTitle());
            stmt.setInt(5, data.getDuration());
            stmt.setString(6, data.getGenre());
            stmt.setString(7, data.getPicturePath());
            stmt.setString(8, data.getLink());
            stmt.setString(9, data.getStartDate().format(DateTimeFormatter.ISO_DATE));
            stmt.setInt(10, data.getId());

            stmt.executeUpdate();
            
            //Brisi sve redatelje na filmu
            stmt = con.prepareCall(DELETE_MOVIE_PEOPLE);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            
            //Ovdje cepam redatelje 
            for (Person director : data.getDirectors()) {
                
                stmt = con.prepareCall(CREATE_DIRECTOR);

                stmt.setString(1, director.getFirstName());
                stmt.setString(2, director.getMiddleName());
                stmt.setString(3, director.getLastName());
                stmt.registerOutParameter(4, Types.INTEGER);

                stmt.executeUpdate();
                PersonID = stmt.getInt(4);

                //Povezi film i redatelja
                stmt = con.prepareCall(CREATE_MOVIE_DIRECTOR);

                stmt.setInt(1, id);
                stmt.setInt(2, PersonID);

                stmt.executeUpdate();
            }

            //Ovdje cepam glumce
            if (data.getActors() != null) {
                for (Person actor : data.getActors()) {
                    stmt = con.prepareCall(CREATE_ACTOR);

                    stmt.setString(1, actor.getFirstName());
                    stmt.setString(2, actor.getMiddleName());
                    stmt.setString(3, actor.getLastName());
                    stmt.registerOutParameter(4, Types.INTEGER);
                    stmt.registerOutParameter(5, Types.INTEGER);

                    stmt.executeUpdate();
                    PersonID = stmt.getInt(5);

                    //Povezi film i glumca
                    stmt = con.prepareCall(CREATE_MOVIE_ACTOR);

                    stmt.setInt(1, id);
                    stmt.setInt(2, PersonID);

                    stmt.executeUpdate();

                }
            }
            
        }
    }
    
    @Override
    public void updateMovieActors(int movieID, Set<Person> actors) throws Exception{
        int PersonID;
        CallableStatement stmt;
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
                        
            //Ovdje cepam glumce
            if (actors != null) {
                for (Person actor : actors) {
                    stmt = con.prepareCall(CREATE_ACTOR);

                    stmt.setString(1, actor.getFirstName());
                    stmt.setString(2, actor.getMiddleName());
                    stmt.setString(3, actor.getLastName());
                    stmt.registerOutParameter(4, Types.INTEGER);
                    stmt.registerOutParameter(5, Types.INTEGER);

                    stmt.executeUpdate();
                    PersonID = stmt.getInt(5);

                    //Povezi film i glumca
                    stmt = con.prepareCall(CREATE_MOVIE_ACTOR);

                    stmt.setInt(1, movieID);
                    stmt.setInt(2, PersonID);

                    stmt.executeUpdate();

                }
            }
            
        }
    }
    
    @Override
    public void deleteAllData() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ALL_DATA)) {
            
            stmt.executeUpdate();
        } 
    }
    
    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            
            stmt.setInt(1, id);
            
            stmt.executeUpdate();
        } 
    }
    
    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        List<Person> directors = new ArrayList<>();
        List<Person> actors = new ArrayList<>();
        CallableStatement stmt;
        ResultSet rs;
        try (Connection con = dataSource.getConnection()) {
            
            //REDATELJI
            stmt = con.prepareCall(SELECT_MOVIE_DIRECTORS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while(rs.next()) {
                directors.add(new Person(rs.getInt(ID_DIRECTOR),rs.getString(FIRST_NAME),rs.getString(MIDDLE_NAME),rs.getString(LAST_NAME)));
            }
            
            //GLUMCI
            stmt = con.prepareCall(SELECT_MOVIE_ACTORS);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                actors.add(new Person(rs.getInt(ID_ACTOR),rs.getString(FIRST_NAME), rs.getString(MIDDLE_NAME), rs.getString(LAST_NAME)));
            }
            
            //FILM
            stmt = con.prepareCall(SELECT_MOVIE);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER),
                        rs.getString(DESCRIPTION),
                        rs.getString(ORIGINAL_TITLE),
                        directors,
                        actors,
                        rs.getInt(DURATION),
                        rs.getString(GENRE),
                        rs.getString(PICTURE_PATH),
                        rs.getString(LINK),
                        LocalDate.parse(rs.getString(START_DATE), DateTimeFormatter.ISO_DATE)));
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {
          
            while (rs.next()) {
                idList.add(rs.getInt(ID_MOVIE));
            }
        }
        
        for (Integer id : idList) {
            movies.add(selectMovie(id).get());
        }
        
        return movies;
    }
    
    @Override
    public List<Person> selectActors() throws SQLException {
        List<Person> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS);
                ResultSet rs = stmt.executeQuery()) {
          
            while (rs.next()) {
                actors.add(new Person(rs.getInt(ID_ACTOR), rs.getString(FIRST_NAME), rs.getString(MIDDLE_NAME), rs.getString(LAST_NAME)));
            }
        }
        
        return actors;
    }
}
