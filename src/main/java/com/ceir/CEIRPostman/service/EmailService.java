package com.ceir.CEIRPostman.service;

import com.ceir.CEIRPostman.util.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    EmailUtil emailUtil;

    @Value("${emailSender}")
    String emailSender;

    @Autowired
    EmailServiceEnhanced emailServiceEnhanced;

    public void startEmailService(ConfigurableApplicationContext ctx) {
        if (emailSender.equalsIgnoreCase("Postfix")) {
            EmailServiceByPostFixCmd fetch = ctx.getBean(EmailServiceByPostFixCmd.class);
            new Thread(fetch).start();
        } else {
            EmailServiceEnhanced fetch1 = ctx.getBean(EmailServiceEnhanced.class);
            new Thread(fetch1).start();
        }
    }
}
