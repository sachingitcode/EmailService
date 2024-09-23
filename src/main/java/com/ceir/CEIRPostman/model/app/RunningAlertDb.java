package com.ceir.CEIRPostman.model.app;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Data
@Entity
@Table(name = "sys_generated_alert")
public class RunningAlertDb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_id")
    private String alertId;
    private String description;
    private Integer status;

    public RunningAlertDb(String alertId, String description, Integer status) {
        super();
        this.alertId = alertId;
        this.description = description;
        this.status = status;
    }


}
