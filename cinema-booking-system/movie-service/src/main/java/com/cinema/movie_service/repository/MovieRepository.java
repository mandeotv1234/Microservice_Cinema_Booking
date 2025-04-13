package com.cinema.movie_service.repository;



import com.cinema.movie_service.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
