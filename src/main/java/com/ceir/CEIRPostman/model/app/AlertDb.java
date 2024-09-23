package com.ceir.CEIRPostman.model.app;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "cfg_feature_alert")
public class AlertDb implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feature;
    @Column(name = "alert_id")
    private String alertId;
    private String description;


}
