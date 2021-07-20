package com.oktenweb.decjavaadv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MovieDto {

  private int movieId;
  private String title;
  private int duration;
  private int directorId;
}
