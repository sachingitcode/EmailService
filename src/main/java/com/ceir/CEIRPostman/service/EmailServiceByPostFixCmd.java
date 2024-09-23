package com.ceir.CEIRPostman.service;

import com.ceir.CEIRPostman.Repository.app.AlertDbRepository;
import com.ceir.CEIRPostman.Repository.app.NotificationRepository;
import com.ceir.CEIRPostman.RepositoryService.EirsResponseParamRepoImpl;
import com.ceir.CEIRPostman.RepositoryService.NotificationRepoImpl;
import com.ceir.CEIRPostman.RepositoryService.RunningAlertRepoService;
import com.ceir.CEIRPostman.RepositoryService.SystemConfigurationDbRepoImpl;
import com.ceir.CEIRPostman.model.app.Notification;
import com.ceir.CEIRPostman.model.app.RunningAlertDb;
import com.ceir.CEIRPostman.util.EmailSendByPostFixCmd;
import com.ceir.CEIRPostman.util.EmailUtil;
import com.ceir.CEIRPostman.util.VirtualIpAddressUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class EmailServiceByPostFixCmd implements Runnable {

    @Autowired
    EmailSendByPostFixCmd emailUtilPostfix;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    NotificationRepository notificationRepo;

    @Autowired
    NotificationRepoImpl notificationRepoImpl;

    @Autowired
    SystemConfigurationDbRepoImpl systemConfigRepoImpl;

    @Value("#{'${type}'.split(',')}")
    public List<String> type;

    @Autowired
    AlertDbRepository alertDbRepository;

    @Autowired
    RunningAlertRepoService alertDbRepo;

    @Autowired
    EirsResponseParamRepoImpl eirsResponseParamRepoImpl;

    @Autowired
    VirtualIpAddressUtil virtualIpAddressUtil;

    int email_tps;
    int emailretrycountValue;
    Integer email_sleep_timer;
    String fromEmail = null;
    int totalMailsent;
    int totalMailNotsent = 0;
    int email_sleep_time;
    String emailBodyFooter = null;
    int email_retry_gap;


    private final Logger log = LogManager.getLogger(EmailServiceByPostFixCmd.class);

    public void run() {

        try {
            email_sleep_time = Integer.parseInt(systemConfigRepoImpl.getValueByTag("email_sleep_time"));
            email_sleep_timer = Integer.parseInt(systemConfigRepoImpl.getValueByTag("email_sleep_timer"));
            fromEmail = systemConfigRepoImpl.getValueByTag("Email_Username");
            email_tps = Integer.parseInt(systemConfigRepoImpl.getValueByTag("email_tps"));
            email_retry_gap = Integer.parseInt(systemConfigRepoImpl.getValueByTag("email_retry_gap"));
            emailretrycountValue = Integer.parseInt(systemConfigRepoImpl.getValueByTag("email_max_retry_count"));
        } catch (Exception e) {
            RunningAlertDb alertDb = new RunningAlertDb("alert1601", "Email Notification Module Key is missing in database configuration", 0);
            alertDbRepo.saveAlertDb(alertDb);
            log.error(e.toString());
        }
        while (true) {
            if (virtualIpAddressUtil.checkVip()) {
                sendEmail();
                sleepForSeconds(email_sleep_timer);
            } else {
                log.info("VIP not found. Sleeping for " + email_sleep_time + " seconds.");
                sleepForSeconds(email_sleep_time);
            }
        }
    }

    private void sendEmail() {
        try {
            log.info("going to fetch data from notification table by status=0 and channel type= " + type);
            List<Notification> notificationData = notificationRepoImpl.dataByStatusAndChannelType(0, type);
            if (!notificationData.isEmpty()) {
                log.info("notification data is not empty and size is " + notificationData.size());
                int sNo = 0;
                for (Notification notification : notificationData) {
                    log.info("notification data id= " + notification.getId() + "With email " + notification.getEmail() + "  from email =" + fromEmail);
                    sNo++;
                    String body = notification.getMessage();
                    emailBodyFooter = eirsResponseParamRepoImpl.getValueByTagAndLang("mail_signature", notification.getMsgLang());
                    if (emailBodyFooter != null) {
                        body = body + "\n" + emailBodyFooter;
                    }
                    var toEmail = getToEmail(notification.getEmail());
                    boolean emailStatus;
                    if ((toEmail != null) && (!(toEmail.toString().isBlank()))
                            && ((StringUtils.isBlank(notification.getAttachment()))
                            || ((!StringUtils.isBlank(notification.getAttachment()))
                            && new File(notification.getAttachment()).isFile()))) {
                        emailStatus = emailUtilPostfix.sendEmailByCmd(toEmail.toString(), fromEmail, notification.getSubject(), body, notification.getAttachment());
                        if (emailStatus) {
                            notification.setStatus(1);
                            totalMailsent++;
                        } else {
                            notification.setRetryCount(notification.getRetryCount() + 1);
                            if (notification.getRetryCount() >= emailretrycountValue) {
                                notification.setStatus(2);
                            }
                            totalMailNotsent++;
                        }
                    } else {
                        log.info("email Id not valid :" + toEmail + "  for Id  " + notification.getId());
                        RunningAlertDb alertDb = new RunningAlertDb("alert1602", alertDbRepository.getByAlertId("alert1602").getDescription().replace("<emailId>", toEmail.toString()), 0);
                        alertDbRepo.saveAlertDb(alertDb);
                        notification.setStatus(2);
                    }
                    notificationRepo.save(notification);
                }
                log.info("total mail sent=  " + totalMailsent + " and email fail to send: " + totalMailNotsent);
            } else {
                log.info("notification data not available to send messages ");
            }
        } catch (Exception e) {
            log.error(" error in sending email " + e);
        }
    }

    public String getToEmail(String toEmails) {
        if (toEmails == null) return null;
        String validEmails = Arrays.stream(toEmails.split(","))
                .filter(emailUtil::emailValidator)
                .collect(Collectors.joining(" "));
        log.info("Valid Emails {}", validEmails);
        return validEmails;
    }

    private static void sleepForSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

