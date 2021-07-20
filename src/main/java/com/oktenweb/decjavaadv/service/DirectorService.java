package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.dto.DirectorListDto;
import com.oktenweb.decjavaadv.entity.Director;

import java.util.List;

public interface DirectorService {

  List<DirectorListDto> getAllDirectors();

  Director createDirector(Director director);

  Director updateDirector(int id, Director director);

  void deleteDirector(int id);

  Director getDirectorById(int id);

}
