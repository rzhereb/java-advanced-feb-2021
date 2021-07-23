package com.oktenweb.decjavaadv.service;

import com.oktenweb.decjavaadv.entity.User;

public interface MailService {

  void sendVerificationCode(User user);

}
