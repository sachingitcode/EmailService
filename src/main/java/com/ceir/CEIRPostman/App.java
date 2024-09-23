package com.ceir.CEIRPostman;

import com.ceir.CEIRPostman.service.EmailService;
import com.ceir.CEIRPostman.service.EmailServiceByPostFixCmd;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ceir.CEIRPostman")
@SpringBootApplication

@EnableConfigurationProperties
@EnableJpaAuditing
@EnableCaching
@EntityScan({"com.ceir.CEIRPostman.model.audit"})
@EnableEncryptableProperties

public class App {
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =SpringApplication.run(App.class, args);

		EmailService emailService = ctx.getBean(EmailService.class);
		emailService.startEmailService(ctx);

	}
}




