package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.dto.MovieCreateDto;
import com.oktenweb.decjavaadv.dto.MoviePage;
import com.oktenweb.decjavaadv.entity.Movie;

public interface MovieService {

  MoviePage getAllMovies(int page, int size);

  MovieCreateDto createMovie(MovieCreateDto movie);

  Movie updateMovie(int id, Movie movie);

  void deleteMovie(int id);

  Movie getMovieById(int id);

  Movie getMovieByTitle(String title);
}
