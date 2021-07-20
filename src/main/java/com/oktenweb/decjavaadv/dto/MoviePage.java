package com.oktenweb.decjavaadv.dto;

import com.oktenweb.decjavaadv.entity.Movie;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MoviePage {

  private List<MovieDto> movies;
  private long totalElements;
  private int currentPage;
  private boolean last;

}
