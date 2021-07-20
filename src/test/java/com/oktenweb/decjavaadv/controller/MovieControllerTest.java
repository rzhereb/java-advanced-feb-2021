package com.oktenweb.decjavaadv.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.oktenweb.decjavaadv.entity.Director;
import com.oktenweb.decjavaadv.entity.Movie;
import com.oktenweb.decjavaadv.service.MovieService;
import com.oktenweb.decjavaadv.validator.MovieValidator;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(MovieController.class)
@ExtendWith(SpringExtension.class)
public class MovieControllerTest {

  @MockBean
  private MovieService movieService;
  @MockBean
  private MovieValidator movieValidator;
  @Autowired
  private MockMvc mockMvc;


  @Test
  public void givenTitleWhenGettingMovieThenReturnMovie() throws Exception {
    Mockito.when(movieService.getMovieByTitle(ArgumentMatchers.anyString()))
        .thenReturn(new Movie(1, "Avengers", 150, new Director()));

    mockMvc.perform(MockMvcRequestBuilders.get("/movie/title/Avengers"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id", CoreMatchers.is(1)))
            .andExpect(jsonPath("$.title", CoreMatchers.is("Avengers")));
  }

}
