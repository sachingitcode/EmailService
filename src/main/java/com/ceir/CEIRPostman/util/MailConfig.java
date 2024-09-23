package com.ceir.CEIRPostman.util;

import com.ceir.CEIRPostman.Repository.app.SystemConfigurationDbRepository;
import com.ceir.CEIRPostman.RepositoryService.SystemConfigurationDbRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail_debug}")
    String maildebug;

    @Value("${mail_protocol}")
    String mailprotocol;  // smtp

    @Value("${mail_auth}")
    String mailauth;  // true

    @Value("${mail_ttl_enable}")
    String mailttl;  // true

    @Value("${mail_ttl_auto}")
    String mailttlauto;  // true

    @Value("${mailsmtpssltrust}")
    String mailsmtpssltrust;

    @Value("${mail_smtp_ssl_protocol}")
    String mailsslprotocol;

    @Autowired
    SystemConfigurationDbRepoImpl systemConfigurationDbRepoImpl;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String fromHost = systemConfigurationDbRepoImpl.getValueByTag("email_host");
        int fromPort = Integer.parseInt(systemConfigurationDbRepoImpl.getValueByTag("email_port"));
        String fromUsername = systemConfigurationDbRepoImpl.getValueByTag("Email_Username");
        String fromPassword = systemConfigurationDbRepoImpl.getValueByTag("Email_Password");
        mailSender.setHost(fromHost);
        mailSender.setPort(fromPort);
        mailSender.setUsername(fromUsername);
        //for production and test  server
        mailSender.setPassword(fromPassword);

        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable", mailttl); //true
        props.put("mail.smtp.auth", mailauth);  // true
        props.put("enable.starttls.auto", mailttlauto); // true 
        props.put("mail.transport.protocol", mailprotocol);
        props.put("mail.smtp.host", fromHost);
        props.put("mail.smtp.ssl.trust", mailsmtpssltrust);
        props.put("mail.debug", maildebug);
        props.put("mail.smtp.ssl.protocols", mailsslprotocol);
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}

