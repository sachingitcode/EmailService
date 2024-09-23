package com.ceir.CEIRPostman.Repository.app;

import com.ceir.CEIRPostman.model.app.SystemConfigurationDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigurationDbRepository extends JpaRepository<SystemConfigurationDb, Long> {

    public SystemConfigurationDb getByTag(String tag);

}
