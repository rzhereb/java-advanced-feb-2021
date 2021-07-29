package com.oktenweb.decjavaadv.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.CharUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.oktenweb.decjavaadv.config.AwsProperties;
import com.oktenweb.decjavaadv.dao.MovieDao;
import com.oktenweb.decjavaadv.dto.MovieCreateDto;
import com.oktenweb.decjavaadv.dto.MovieDto;
import com.oktenweb.decjavaadv.dto.MoviePage;
import com.oktenweb.decjavaadv.entity.Movie;
import com.oktenweb.decjavaadv.exceptions.ItemNotFoundException;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

  @Autowired
  private MovieDao movieDao;

  @Autowired
  private DirectorService directorService;

  @Autowired
  private AwsProperties awsProperties;

  @Autowired
  private S3Client s3Client;

  @Autowired
  private S3AsyncClient s3AsyncClient;

  @Override
  public MoviePage getAllMovies(int page, int size) {
    final Page<Movie> movies = movieDao.findAll(PageRequest.of(page, size));
    final MoviePage moviePage = new MoviePage();
    final List<Movie> content = movies.getContent();
    moviePage.setMovies(content.stream().map(movie -> {
      MovieDto movieDto = new MovieDto();
      movieDto.setMovieId(movie.getId());
      movieDto.setDuration(movie.getDuration());
      movieDto.setTitle(movie.getTitle());
      movieDto.setDirectorId(movie.getDirector().getId());
      return movieDto;
    }).collect(Collectors.toList()));
    moviePage.setCurrentPage(movies.getNumber());
    moviePage.setLast(movies.isLast());
    moviePage.setTotalElements(movies.getTotalElements());
    return moviePage;
  }

  @Override
  public MovieCreateDto createMovie(MovieCreateDto movie, MultipartFile multipartFile) {
    if (!CharUtils.isAsciiAlphaUpper(movie.getTitle().charAt(0))) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title should start with capital letter!");
    }
    Movie movieDb = new Movie();
    movieDb.setTitle(movie.getTitle());
    movieDb.setDuration(movie.getDuration());
    movieDb.setDirector(directorService.getDirectorById(movie.getDirectorId()));
    movieDao.saveAndFlush(movieDb);
    movie.setId(movieDb.getId());

    PutObjectRequest putObjectRequest = PutObjectRequest
        .builder()
        .bucket(awsProperties.getBucketName())
        .key(String.valueOf(movieDb.getId()))
        .build();
    try {
//      final PutObjectResponse putObjectResponse = s3Client
//          .putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
      final CompletableFuture<PutObjectResponse> future = s3AsyncClient
          .putObject(putObjectRequest, AsyncRequestBody.fromBytes(multipartFile.getBytes()));
      future.whenComplete((resolve, reject) -> {
        if (resolve != null) {
          log.info("{}", resolve);
        }
        if (reject != null) {
          log.warn("{}", reject.getLocalizedMessage());
        }
      });
    } catch (IOException e) {
      log.warn("{}", e.getMessage());
    }
    return movie;
  }

  @Override
  public Movie updateMovie(int id, Movie movie) {
    movie.setId(id);
    if (!movieDao.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No movie found");
    }
    return movieDao.saveAndFlush(movie);
  }

  @Override
  public void deleteMovie(int id) {
    if (!movieDao.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No movie found");
    }
    movieDao.deleteById(id);
  }

  @Override
  public Movie getMovieById(int id) {
    return movieDao.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No movie with id:" + id));
  }

  @Override
  public Movie getMovieByTitle(String title) {
    final Optional<Movie> movie = movieDao.findByTitle(title);
    return movie.orElseThrow(() -> new ItemNotFoundException("Movie", "title", title));
  }

  @Override
  public byte[] getPoster(int id) {
    try {
      final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(awsProperties.getBucketName())
          .key(String.valueOf(id))
          .build();
      final ResponseInputStream<GetObjectResponse> ris = s3Client.getObject(getObjectRequest);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int len;
      while ((len = ris.read(buffer, 0, buffer.length)) != -1) {
        baos.write(buffer, 0, len);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new byte[0];
  }
}
