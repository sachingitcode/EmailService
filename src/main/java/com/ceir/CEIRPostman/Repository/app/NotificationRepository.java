package com.ceir.CEIRPostman.Repository.app;
import java.util.List;
import java.util.Queue;

import org.hibernate.annotations.ParamDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceir.CEIRPostman.model.app.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification>{

	public List<Notification>  	findByStatusAndChannelTypeInOrderByChannelTypeDesc(int status,List<String> channelType);
}
