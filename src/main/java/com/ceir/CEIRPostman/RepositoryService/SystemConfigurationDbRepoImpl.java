package com.ceir.CEIRPostman.RepositoryService;

import com.ceir.CEIRPostman.Repository.app.AlertDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.CEIRPostman.Repository.app.SystemConfigurationDbRepository;
import com.ceir.CEIRPostman.model.app.RunningAlertDb;
import com.ceir.CEIRPostman.model.app.SystemConfigurationDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SystemConfigurationDbRepoImpl {

    @Autowired
    RunningAlertRepoService alertDbRepo;
    @Autowired
    AlertDbRepository alertDbRepository;

    @Autowired
    SystemConfigurationDbRepository systemConfigurationDbRepo;
    private final Logger log = LogManager.getLogger(SystemConfigurationDbRepoImpl.class);

    public String getValueByTag(String tag) {
        try {
            var value = systemConfigurationDbRepo.getByTag(tag).getValue();
            if (value == null || value.isBlank()) {
                log.error("No value found for tag " + tag + "  ");
                RunningAlertDb alertDb = new RunningAlertDb("alert1601", alertDbRepository.getByAlertId("alert1601").getDescription().replace("<Key>", tag), 0);
                alertDbRepo.saveAlertDb(alertDb);
                System.exit(0);
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("No value found for tag " + tag + " # Error : " + e.toString());
            RunningAlertDb alertDb = new RunningAlertDb("alert1601", alertDbRepository.getByAlertId("alert1601").getDescription().replace("<Key>", tag), 0);
            alertDbRepo.saveAlertDb(alertDb);
            System.exit(0);
            return null;
        }
    }
}
