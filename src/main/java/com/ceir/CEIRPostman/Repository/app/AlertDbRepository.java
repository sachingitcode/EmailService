package com.ceir.CEIRPostman.Repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceir.CEIRPostman.model.app.AlertDb;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertDbRepository extends JpaRepository<AlertDb, Long>, JpaSpecificationExecutor<AlertDb> {

	public AlertDb getByAlertId(String alertId);
}
