package com.isacc.mail.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.isacc.mail.api.dto.ApiResult;
import com.isacc.mail.infra.config.AzkabanProperties;
import com.isacc.mail.infra.constant.ExecFlowStatusConstants;
import com.isacc.mail.infra.util.FreemarkerUtil;
import com.isacc.mail.service.AzkabanMailService;
import com.isacc.mail.service.AzkabanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * description
 *
 * @author isacc 2019/08/01 16:24
 * @since 1.0
 */
@Service
@Slf4j
public class AzkabanMailServiceImpl implements AzkabanMailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final AzkabanService azkabanService;
    private final AzkabanProperties azkabanProperties;
    private final RedisTemplate redisTemplate;
    private static final String EXEC_IDS = "execIds";

    public AzkabanMailServiceImpl(JavaMailSender javaMailSender, AzkabanService azkabanService, AzkabanProperties azkabanProperties, RedisTemplate redisTemplate) {
        this.javaMailSender = javaMailSender;
        this.azkabanService = azkabanService;
        this.azkabanProperties = azkabanProperties;
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void azkabanSendMail() {
        // 登录
        ApiResult<Object> loginResult = azkabanService.login();
        if (!loginResult.getResult()) {
            return;
        }
        log.info("登录");
        String sessionId = String.valueOf(loginResult.getContent());
        final Map<String, String> monitorFlowMap = azkabanProperties.getMonitorFlowMap();
        for (Map.Entry<String, String> entry : monitorFlowMap.entrySet()) {
            // 获取正在执行的任务流
            final ApiResult<Object> runningExecutionsResult = azkabanService.fetchRunningExecutions(sessionId,
                    entry.getKey(),
                    entry.getValue());
            if (!runningExecutionsResult.getResult()) {
                log.info(String.format("获取正在运行的%s任务流信息详情失败", entry.getKey()));
                return;
            }
            log.info(String.format("获取正在运行的%s任务流", entry.getKey()));
            // 获取正在执行的execIds 测试使用final List execIds = Collections.singletonList(109)
            final Map runningExecutions = (Map) runningExecutionsResult.getContent();
            final List execIds = (List) runningExecutions.get(EXEC_IDS);
            if (CollectionUtils.isEmpty(execIds)) {
                log.info(String.format("目前没有正在运行的%s任务流", entry.getKey()));
            } else {
                log.info(String.format("=======%s任务流正在执行，获取执行详情=======", entry.getKey()));
                redisTemplate.opsForSet().add(EXEC_IDS, execIds.toArray());
            }
            // 发送邮件
            azkabanHandler(entry, sessionId);
        }
        log.info("监控中……");
    }

    @SuppressWarnings("unchecked")
    private void azkabanHandler(Map.Entry<String, String> entry, String sessionId) {
        final Set execidSet = redisTemplate.opsForSet().members(EXEC_IDS);
        if (execidSet != null) {
            for (Object o : execidSet) {
                final Integer execId = (Integer) o;
                final ApiResult<Object> execFlowResult = azkabanService.fetchExecFlow(sessionId, execId);
                if (!execFlowResult.getResult()) {
                    log.info(String.format("根据%d获取%s任务流失败", execId, entry.getKey()));
                    return;
                }
                final Map execFlowContent = (Map) execFlowResult.getContent();
                final String status = (String) execFlowContent.get("status");
                switch (status) {
                    case ExecFlowStatusConstants.FAILED:
                        // 发送失败邮件
                        log.info(String.format("=======%s任务流执行失败=======", entry.getKey()));
                        sendEmail(false, execFlowContent);
                        // 邮件发送后删除redis
                        redisTemplate.opsForSet().remove(EXEC_IDS, execId);
                        break;
                    case ExecFlowStatusConstants.SUCCEEDED:
                        // 发送成功邮件
                        log.info(String.format("=======%s任务流执行成功=======", entry.getKey()));
                        sendEmail(true, execFlowContent);
                        // 邮件发送后删除redis
                        redisTemplate.opsForSet().remove(EXEC_IDS, execId);
                        break;
                    case ExecFlowStatusConstants.KILLED:
                        //  被取消 删除redis
                        log.info(String.format("=======%s任务流被杀死了=======", entry.getKey()));
                        redisTemplate.opsForSet().remove(EXEC_IDS, execId);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void sendEmail(boolean isSuccess, Map execFlowContent) {
        final String[] toMailArray = azkabanProperties.getToMailArray();
        execFlowContent.put("profile", azkabanProperties.getProfile());
        execFlowContent.put("duration", FreemarkerUtil.timestamp2String(
                (long) execFlowContent.get("endTime") - (long) execFlowContent.get("startTime")));
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 邮件发送者 发信人需要和spring.mail.username配置的一样否则报错
            mimeMessageHelper.setFrom(from);
            // 邮件接受者
            mimeMessageHelper.setTo(toMailArray);
            // 发送内容 第二个参数true表示使用HTML语言来编写邮件
            String htmlContent;
            if (isSuccess) {
                // 主题
                mimeMessageHelper.setSubject(String.format("%s数据库%s任务成功通知", azkabanProperties.getProfile(), execFlowContent.get("project")));
                final ApiResult successEmail = FreemarkerUtil.createStringTemplate(
                        execFlowContent,
                        azkabanProperties.getMailTemplatePath(),
                        azkabanProperties.getMailSuccessFtl());
                htmlContent = (String) successEmail.getContent();
                mimeMessageHelper.setText(htmlContent, true);
            } else {
                // 主题
                mimeMessageHelper.setSubject(String.format("警告！%s数据库%s任务失败通知", azkabanProperties.getProfile(), execFlowContent.get("project")));
                final ApiResult failEmail = FreemarkerUtil.createStringTemplate(
                        execFlowContent,
                        azkabanProperties.getMailTemplatePath(),
                        azkabanProperties.getMailFailFtl());
                htmlContent = (String) failEmail.getContent();
                mimeMessageHelper.setText(htmlContent, true);
            }
            // 发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("send email error", e);
        }
    }
}
