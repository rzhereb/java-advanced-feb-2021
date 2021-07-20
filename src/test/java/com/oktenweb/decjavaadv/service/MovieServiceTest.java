package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.dao.MovieDao;
import com.oktenweb.decjavaadv.dto.MovieCreateDto;
import com.oktenweb.decjavaadv.dto.MovieDto;
import com.oktenweb.decjavaadv.dto.MoviePage;
import com.oktenweb.decjavaadv.entity.Director;
import com.oktenweb.decjavaadv.entity.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

  @Mock
  private MovieDao movieDao;
  @Mock
  private DirectorService directorService;
  @InjectMocks
  private MovieServiceImpl movieService;

  @Test
  public void givenPageAndSizeWhenGettingMoviesThenReturnMovies() {
    List<Movie> movies = new ArrayList<>();
    movies.add(new Movie(1, "Casino", 140, generateDirector(1)));
    movies.add(new Movie(2, "Kill Bill", 144, generateDirector(1)));
    final PageImpl<Movie> moviePage = new PageImpl<>(movies);
    Mockito.when(movieDao.findAll(ArgumentMatchers.any(PageRequest.class)))
        .thenReturn(moviePage);

    List<MovieDto> movieDtos = new ArrayList<>();
    final MovieDto movieDto1 = new MovieDto();
    movieDto1.setMovieId(1);
    movieDto1.setDirectorId(1);
    movieDto1.setDuration(140);
    final MovieDto movieDto2 = new MovieDto();
    movieDto2.setMovieId(2);
    movieDto2.setDirectorId(1);
    movieDtos.add(movieDto1);
    movieDtos.add(movieDto2);

    MoviePage expectedResult = new MoviePage();
    expectedResult.setMovies(movieDtos);
    expectedResult.setTotalElements(2);

    final MoviePage actualResult = movieService.getAllMovies(0, 2);

    Assertions.assertEquals(expectedResult.getTotalElements(), actualResult.getTotalElements());
    Assertions.assertEquals(expectedResult.getMovies().get(0).getMovieId(),
        actualResult.getMovies().get(0).getMovieId());
    Assertions.assertEquals(expectedResult.getMovies().get(0).getDuration(),
        actualResult.getMovies().get(0).getDuration());

  }

  @Test
  public void givenTitleStartingWithSmallLetterWhenSavingMovieThenThrowException() {
    MovieCreateDto movie = new MovieCreateDto();
    movie.setTitle("so");
    Assertions.assertThrows(ResponseStatusException.class, () -> movieService.createMovie(movie));
  }

  private Director generateDirector(int id) {
    return new Director(id, "Tarantino", null, null);
  }


}
