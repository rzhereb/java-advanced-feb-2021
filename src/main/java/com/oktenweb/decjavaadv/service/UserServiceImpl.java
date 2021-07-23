package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.dao.UserDao;
import com.oktenweb.decjavaadv.dto.UserDto;
import com.oktenweb.decjavaadv.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

  private final UserDao userDao;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDao.findByUsername(username);
  }

  @Override
  public String createUser(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setRole("ROLE_USER");
    user.setEmail(userDto.getEmail());
    user.setEnabled(false);
    user.setVerificationCode(RandomStringUtils.randomAlphabetic(10));
    userDao.save(user);

    mailService.sendVerificationCode(user);

    return user.getUsername();
  }

  @Override
  public void verifyUser(String code) {
    final Optional<User> optionalUser = userDao.findByVerificationCode(code);
    final User user = optionalUser
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user for code: " + code));
    user.setEnabled(true);
    user.setVerificationCode(null);
    userDao.flush();
    log.info("User {} has been activated", user.getEmail());
  }
}
