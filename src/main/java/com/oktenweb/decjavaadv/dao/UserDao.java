package com.oktenweb.decjavaadv.dao;

import com.oktenweb.decjavaadv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {

  User findByUsername(String username);

  Optional<User> findByVerificationCode(String code);
}
