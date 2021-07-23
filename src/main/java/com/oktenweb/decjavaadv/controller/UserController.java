package com.oktenweb.decjavaadv.controller;

import com.oktenweb.decjavaadv.dto.UserDto;
import com.oktenweb.decjavaadv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/user")
  public String register(@RequestBody UserDto userDto) {
    return userService.createUser(userDto);
  }

  @GetMapping("/user/verification")
  public void verifyUser(@RequestParam String code) {
    userService.verifyUser(code);
  }
}
