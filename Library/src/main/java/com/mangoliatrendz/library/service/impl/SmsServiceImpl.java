package com.mangoliatrendz.library.service.impl;

import java.text.DecimalFormat;
import java.util.Random;

import com.mangoliatrendz.library.Exception.OtpSendException;
import com.mangoliatrendz.library.customerconfig.TwilioConfig;
import com.mangoliatrendz.library.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private TwilioConfig twilioConfig;

    public SmsServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public String generateOtp() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

    @Override
    public void sendOtp(String otp) {
        try {
            PhoneNumber to = new PhoneNumber("+919567712456");//to
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from
            String otpMessage = "Dear Customer , Your OTP is  " + otp + " for sending sms through Spring boot application. Thank You.";
            Message message = Message
                    .creator(to, from,
                            otpMessage)
                    .create();
        } catch (Exception e) {
            throw new OtpSendException("Error sending OTP: " + e.getMessage()) ;
        }

    }


}
