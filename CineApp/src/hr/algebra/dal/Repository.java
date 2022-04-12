/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Mihael
 */
public interface Repository {
    
    void createAdminUser() throws Exception;
    void createUser(String username, String password) throws Exception;
    Optional<User> selectUser(String username, String password) throws Exception;
    void createMovie(Movie movie) throws Exception;
    void createMovies(List<Movie> movies) throws Exception;
    boolean createActor(Person actor) throws Exception;
    void updateMovie(int id, Movie data) throws Exception;
    void updateMovieActors(int movieID, Set<Person> actors) throws Exception;
    void deleteMovie(int id) throws Exception;
    void deleteAllData() throws Exception;
    Optional<Movie> selectMovie(int id) throws Exception;
    List<Movie> selectMovies() throws Exception;
    List<Person> selectActors() throws Exception;
}
