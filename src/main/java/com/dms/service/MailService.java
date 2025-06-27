package com.dms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            logger.info(" Sending mail to {}", to);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("dhumalanil9@gmail.com"); // make sure this matches spring.mail.username
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);

            mailSender.send(msg);
            logger.info(" Mail successfully sent to {}", to);

        } catch (Exception e) {
            logger.error(" Failed to send email", e);
        }
    }
}

