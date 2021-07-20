package com.oktenweb.decjavaadv.controller;

import com.oktenweb.decjavaadv.dto.DirectorListDto;
import com.oktenweb.decjavaadv.entity.Director;
import com.oktenweb.decjavaadv.service.DirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/director")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {

  private final DirectorService directorService;

  @GetMapping
  public List<DirectorListDto> getDirectors() {
    return directorService.getAllDirectors();
  }

  @GetMapping("/{id}")
  public Director getDirectorById(@PathVariable int id) {
    return directorService.getDirectorById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Director insertDirector(@RequestBody @Valid Director director) {
    log.info("Handling POST request for object {}", director);
    return directorService.createDirector(director);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Director updateDirector(@PathVariable int id, @RequestBody @Valid Director director) {
    return directorService.updateDirector(id, director);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDirector(@PathVariable int id) {
    directorService.deleteDirector(id);
  }
}
