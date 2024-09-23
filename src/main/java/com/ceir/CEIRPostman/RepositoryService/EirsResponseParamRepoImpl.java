package com.ceir.CEIRPostman.RepositoryService;

import com.ceir.CEIRPostman.Repository.app.AlertDbRepository;
import com.ceir.CEIRPostman.Repository.app.EirsResponseParamRepository;
import com.ceir.CEIRPostman.model.app.RunningAlertDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EirsResponseParamRepoImpl {

    @Autowired
    RunningAlertRepoService alertDbRepo;
    @Autowired
    AlertDbRepository alertDbRepository;

    @Autowired
    EirsResponseParamRepository eirsResponseParamRepository;

    private final Logger log = LogManager.getLogger(EirsResponseParamRepoImpl.class);

    public String getValueByTagAndLang(String tag, String language) {
        try {
            var value = eirsResponseParamRepository.getByTagAndLanguage(tag, language).getValue();
            if (value == null || value.isBlank()) {
                log.error("No value found for tag " + tag + "  ");
                RunningAlertDb alertDb = new RunningAlertDb("alert1601", alertDbRepository.getByAlertId("alert1601").getDescription().replace("<Key>", tag), 0);
                alertDbRepo.saveAlertDb(alertDb);
                System.exit(0);
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("Details not able to access for tag " + tag + ".  Error : " + e.toString());
            RunningAlertDb alertDb = new RunningAlertDb("alert1601", alertDbRepository.getByAlertId("alert1601").getDescription().replace("<Key>", tag), 0);
            alertDbRepo.saveAlertDb(alertDb);
            System.exit(0);
            return null;
        }
    }
}
