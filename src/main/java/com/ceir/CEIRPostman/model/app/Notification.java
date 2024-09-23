package com.ceir.CEIRPostman.model.app;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "channel_type")
    private String channelType;

    @Column(name = "msg_lang")
    private String msgLang;

    @Column(name = "retry_count")
    private Integer retryCount;

    private String message;
    private Integer status;
    private String subject;
    private String email;
    private String attachment;

}
