package com.oktenweb.decjavaadv.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DirectorListDto {

  private int directorId;
  private String name;
  private List<Integer> movies;

}
