package com.isacc.mail.infra.config;

import java.util.List;
import java.util.Map;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/07/10 15:37
 */
@Data
@Configuration
@ConfigurationProperties(prefix = AzkabanProperties.PROPERTY_PREFIX)
public class AzkabanProperties {
    public static final String PROPERTY_PREFIX = "azkaban";

    /**
     * azkaban环境 dev prod
     */
    private String profile;
    /**
     * 主机名
     */
    private String host;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 发动的邮件模板path
     */
    private String mailTemplatePath;
    /**
     * 成功邮件的模板
     */
    private String mailSuccessFtl;
    /**
     * 失败邮件的模板
     */
    private String mailFailFtl;
    /**
     * 需要发送的邮箱列表集合
     */
    private String[] toMailArray;
    /**
     * 需要监控的任务流
     */
    private Map<String, String> monitorFlowMap;

}
