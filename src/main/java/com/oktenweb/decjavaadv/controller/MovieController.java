package com.oktenweb.decjavaadv.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oktenweb.decjavaadv.dto.MovieCreateDto;
import com.oktenweb.decjavaadv.dto.MoviePage;
import com.oktenweb.decjavaadv.entity.Movie;
import com.oktenweb.decjavaadv.service.MovieService;
import com.oktenweb.decjavaadv.validator.MovieValidator;

@RestController
//@Controller
//@Component
//@Repository
//@Service
//@Bean
//@ControllerAdvice
//@RestControllerAdvice
@RequestMapping(value = "/movie")
public class MovieController {

  private static final Logger LOG = LoggerFactory.getLogger(MovieController.class);

  @Autowired
  private MovieService movieService;

  @Autowired
  private MovieValidator movieValidator;

//  @RequestMapping(value = "/movie", method = RequestMethod.GET)
  @GetMapping
  public MoviePage getMovies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
    return movieService.getAllMovies(page, size);
  }

  @GetMapping("/{id}")
  public Movie getMovieById(@PathVariable int id) {
    return movieService.getMovieById(id);
  }

  @GetMapping("/title/{title}")
  public Movie getMovieByTitle(@PathVariable String title) {
    return movieService.getMovieByTitle(title);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public MovieCreateDto insertMovie(@ModelAttribute @Valid MovieCreateDto movie, MultipartFile multipartFile) {
    LOG.info("Handling POST request for object {}", movie);
    return movieService.createMovie(movie, multipartFile);
  }

  @GetMapping(value = "/poster/{id}")
  public ResponseEntity<byte[]> getPoster(@PathVariable int id) {
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_JPEG)
        .body(movieService.getPoster(id));
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Movie updateMovie(@PathVariable int id, @RequestBody @Valid Movie movie) {
    return movieService.updateMovie(id, movie);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMovie(@PathVariable int id) {
    movieService.deleteMovie(id);
  }


//  @InitBinder
//  public void initBinder(WebDataBinder dataBinder) {
//    dataBinder.addValidators(movieValidator);
//  }
}

//               MySQL        REST
//// 1. create   INSERT       POST
//// 2. read     SELECT       GET
//// 3. update   UPDATE       PUT / PATCH
//// 4. delete   DELETE       DELETE
