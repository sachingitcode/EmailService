package com.ceir.CEIRPostman.Repository.app;

import com.ceir.CEIRPostman.model.app.RunningAlertDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningAlertDbRepo extends JpaRepository<RunningAlertDb, Long> {
    public RunningAlertDb save(RunningAlertDb alertDb);
}
