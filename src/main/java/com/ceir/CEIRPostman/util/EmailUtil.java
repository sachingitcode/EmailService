package com.ceir.CEIRPostman.util;

import com.ceir.CEIRPostman.Repository.app.AlertDbRepository;
import com.ceir.CEIRPostman.RepositoryService.RunningAlertRepoService;
import com.ceir.CEIRPostman.model.app.RunningAlertDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;

@Service
public class EmailUtil {

    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MailSender mailSender;

    SimpleMailMessage[] messages;


    @Autowired
    RunningAlertRepoService alertDbRepo;

    @Autowired
    AlertDbRepository alertDbRepository;

    int messageIndex = 0;


    public boolean emailValidator(String email) {
        logger.info(" email validation start for email " + email);
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (Exception e) {
            logger.warn("this email " + email + " is incorrect. Error:  " + e);
            return false;
        }
    }

    public void setSize(int batch) {
        messages = new SimpleMailMessage[batch];
    }

    public boolean sendEmail(String toAddress, String fromAddress, String subject, String msgBody, int totalData, int dataRead, Integer emailPerformaceTPS) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(msgBody);
        try {
            messages[0] = simpleMailMessage;
            logger.info(  "Mail : -: " + messages[messageIndex] );
            {
                try {
                    long start_time = System.currentTimeMillis();
                    mailSender.send(messages);
                    checkPerformanceTps(toAddress, emailPerformaceTPS, start_time);
                } catch (Exception e) {
                    logger.error("error occur exceptions- For email:[" + toAddress + "] - Error " + e.getLocalizedMessage() + "...Email send exception,Error" + e.toString());
                    RunningAlertDb alertDb = new RunningAlertDb("alert1602", alertDbRepository.getByAlertId("alert1602").getDescription().replace("<emailId>", toAddress), 0);
                    alertDbRepo.saveAlertDb(alertDb);
                    return Boolean.FALSE;
                }
            }
            logger.info(" Email successfully send ");
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.error(e.toString() + " error occur-" + e.getMessage());
            return Boolean.FALSE;
        }
    }


    public boolean sendEmailWithAttactment(String toAddress, String fromAddress, String subject, String msgBody, String attachment, Integer emailPerformaceTPS) {
        var message = javaMailSender.createMimeMessage();
        try {
            logger.info("Going to send Email : " + toAddress + " . From:" + fromAddress + ",Subject:" + subject + ",Body" + msgBody + ",Attachment:" + attachment);
            long start_time = System.currentTimeMillis();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(msgBody);
            FileSystemResource file = new FileSystemResource(attachment);
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(message);
            logger.info("Send Email Status::  TRUE");
            checkPerformanceTps(toAddress, emailPerformaceTPS, start_time);
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.error(e.toString() + "; error occur;;" + e.getMessage());
            RunningAlertDb alertDb = new RunningAlertDb("alert1602", alertDbRepository.getByAlertId("alert1602").getDescription().replace("<emailId>", toAddress), 0);
            alertDbRepo.saveAlertDb(alertDb);
            return Boolean.FALSE;
        }
    }

    private void checkPerformanceTps(String toAddress, Integer emailPerformaceTPS, long start_time) {
        long end_time = System.currentTimeMillis();
        if ((end_time - start_time) > emailPerformaceTPS) {
            logger.warn("Performance issue for Email Sending : Time Taken " + (end_time - start_time));
            try {
                RunningAlertDb alertDb = new RunningAlertDb("alert1604", alertDbRepository.getByAlertId("alert1604").getDescription().replace("<emailId>", toAddress), 0);
                alertDbRepo.saveAlertDb(alertDb);
            } catch (Exception e) {
                logger.warn("Not able to raise alert. due to " + e);
            }
        }
    }

}
