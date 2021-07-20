package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.dao.DirectorDao;
import com.oktenweb.decjavaadv.dto.DirectorListDto;
import com.oktenweb.decjavaadv.entity.Director;
import com.oktenweb.decjavaadv.entity.Movie;
import com.oktenweb.decjavaadv.exceptions.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectorServiceImpl implements DirectorService {

  private final DirectorDao directorDao;

  public DirectorServiceImpl(DirectorDao directorDao) {
    this.directorDao = directorDao;
  }

  @Override
  public List<DirectorListDto> getAllDirectors() {
    final List<Director> directorDaoAll = directorDao.myFindAll();
    final List<DirectorListDto> collect = directorDaoAll.stream()
        .map(director -> {
          DirectorListDto directorListDto = new DirectorListDto();
          directorListDto.setDirectorId(director.getId());
          directorListDto.setName(director.getName());

          final List<Integer> ids = director.getMovies().stream()
              .map(Movie::getId)
              .collect(Collectors.toList());
          directorListDto.setMovies(ids);
          return directorListDto;
        }).collect(Collectors.toList());
    return collect;
  }

  @Override
  public Director createDirector(Director director) {
    return directorDao.save(director);
  }

  @Override
  public Director updateDirector(int id, Director director) {
    director.setId(id);
    if (!directorDao.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No director found");
    }
    return directorDao.saveAndFlush(director);
  }

  @Override
  public void deleteDirector(int id) {
    directorDao.deleteById(id);
  }

  @Override
  public Director getDirectorById(int id) {
    return directorDao.findById(id).orElseThrow(() -> new ItemNotFoundException("director", "id", String.valueOf(id)));
  }
}
