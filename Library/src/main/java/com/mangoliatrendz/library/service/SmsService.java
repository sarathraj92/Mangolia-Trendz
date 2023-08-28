package com.mangoliatrendz.library.service;

public interface SmsService {


    String generateOtp();

    void sendOtp(String otp);
}
