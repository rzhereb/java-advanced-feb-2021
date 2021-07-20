package com.oktenweb.decjavaadv.dao;

import com.oktenweb.decjavaadv.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirectorDao extends JpaRepository<Director, Integer> {

  @Query("select d from Director d join fetch d.movies")
  List<Director> myFindAll();
}
