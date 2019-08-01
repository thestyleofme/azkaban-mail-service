package com.isacc.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/07/10 14:50
 */
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
@EnableScheduling
public class MailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class, args);
    }

}
