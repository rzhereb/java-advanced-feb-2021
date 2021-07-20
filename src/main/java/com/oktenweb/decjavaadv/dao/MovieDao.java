package com.oktenweb.decjavaadv.dao;

import com.oktenweb.decjavaadv.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovieDao extends JpaRepository<Movie, Integer> {

  @Query("SELECT m FROM Movie m WHERE m.title LIKE :title")
  Optional<Movie> findByTitle(String title);

}
