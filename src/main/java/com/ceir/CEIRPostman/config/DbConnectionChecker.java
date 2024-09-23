/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceir.CEIRPostman.config;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class DbConnectionChecker {

    private final Logger logger = LogManager.getLogger(DbConnectionChecker.class);

    public void checkDbConnection(DataSource dataSource, String dbName) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Database connection for " + dbName + " is successful!");
        } catch (SQLException e) {
            logger.error("alert1603:Email Notification Module not able to connect with "+dbName+" database");
            e.printStackTrace();
        }
    }
}
