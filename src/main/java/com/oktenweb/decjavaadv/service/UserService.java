package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.dto.UserDto;

public interface UserService {

  String createUser(UserDto userDto);

  void verifyUser(String code);
}
