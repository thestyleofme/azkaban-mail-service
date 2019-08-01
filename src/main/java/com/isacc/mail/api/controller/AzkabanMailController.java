package com.isacc.mail.api.controller;

import com.isacc.mail.service.AzkabanMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/07/10 14:50
 */
@Slf4j
@RestController
public class AzkabanMailController {

    private final AzkabanMailService azkabanMailService;

    public AzkabanMailController(AzkabanMailService azkabanMailService) {
        this.azkabanMailService = azkabanMailService;
    }

    @GetMapping("/send")
    @Scheduled(cron = "*/5 * * * * ?")
    public void sendEmail() {
        // */30 * * * * ?
        // 0 0/10 * * * ?
        azkabanMailService.azkabanSendMail();
    }

}
