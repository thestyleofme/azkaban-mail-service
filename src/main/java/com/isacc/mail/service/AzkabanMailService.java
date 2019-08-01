package com.isacc.mail.service;

/**
 * description
 *
 * @author isacc 2019/08/01 16:22
 * @since 1.0
 */
public interface AzkabanMailService {

    /**
     * 监控azkaban任务流，任务流失败完成后邮件提醒
     *
     * @author isacc 2019/8/1 16:23
     */
    void azkabanSendMail();
}
