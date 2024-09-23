package com.ceir.CEIRPostman.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class EmailSendByPostFixCmd {

    private final Logger logger = LogManager.getLogger(getClass());

    public boolean sendEmailByCmd(String toEmail, String fromEmail, String subject, String body, String attach) {
        try {
            var attachmentString = StringUtils.isBlank(attach) ? "" : " -a '" + attach + "' ";

            String command = "echo ' " + body + " '  | mailx -r '" + fromEmail + "'  " + attachmentString + "   -s ' " + subject + "'  " + toEmail + " ";
            logger.info("Email Cmd3:::" + command);
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            int exitCode = p.waitFor();
            logger.info("ExitCode :: {} " , exitCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            String response = null;
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            logger.info("Response :: " + response);
        } catch (Exception ex) {
            logger.error("Not able to execute : " + ex.getLocalizedMessage() + "");
        }
        return true;
    }

}
