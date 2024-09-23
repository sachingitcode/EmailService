package com.ceir.CEIRPostman.RepositoryService;

import com.ceir.CEIRPostman.Repository.app.NotificationRepository;
import com.ceir.CEIRPostman.model.app.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class NotificationRepoImpl {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SystemConfigurationDbRepoImpl systemConfigRepoImpl;

    private final Logger log = LogManager.getLogger(NotificationRepoImpl.class);

    public List<Notification> dataByStatusAndChannelType(int status, List<String> type) {
        try {
            int email_retry_gap = Integer.parseInt(systemConfigRepoImpl.getValueByTag("email_retry_gap"));
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Notification> notificationList = notificationRepository.findByStatusAndChannelTypeInOrderByChannelTypeDesc(status, type);
            log.info("Notification db list size  " + notificationList.size());
            List<Notification> notificationNew = new ArrayList<Notification>();
            if (notificationList.size() != 0) {
            notificationNew = notificationList.stream()
                    .filter( notif -> notif.getRetryCount() == 0
                            || ( notif.getRetryCount() > 0
                    && (notif.getModifiedOn().plusSeconds(email_retry_gap).isBefore(currentDateTime))))
                     .collect(toList());
            }
            for (Notification noti : notificationNew) {
                log.info("Notification :  id {} ,ModifiedOn {} ,Email {} , Type {}  ", noti.getId(), noti.getModifiedOn(), noti.getEmail(), noti.getChannelType());
            }
            return notificationNew;
        } catch (Exception e) {
            log.error("error occurs while fetch notification data" + e.toString());
            return new ArrayList<Notification>();
        }
    }

}
