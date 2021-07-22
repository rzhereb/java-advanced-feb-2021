package com.oktenweb.decjavaadv.controller;

import com.oktenweb.decjavaadv.dto.AuthRequest;
import com.oktenweb.decjavaadv.dto.AuthResponse;
import com.oktenweb.decjavaadv.dto.UserDto;
import com.oktenweb.decjavaadv.service.JwtService;
import com.oktenweb.decjavaadv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/user")
  public String register(@RequestBody UserDto userDto) {
    return userService.createUser(userDto);
  }

  @PostMapping("/token")
  public AuthResponse generateToken(@RequestBody AuthRequest authRequest) {
    final Authentication authenticate = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
    if (!authenticate.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user with such username: " + authRequest.getUsername());
    }
    final String token =
        jwtService.generateToken(authRequest.getUsername(), "My Token");
    return new AuthResponse(token);
  }
}
