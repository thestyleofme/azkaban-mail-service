package com.isacc.mail.infra.util;

import java.io.File;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.isacc.mail.MailServiceApplication;
import com.isacc.mail.api.dto.ApiResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

/**
 * <p>
 * Freemarker Utils
 * </p>
 *
 * @author isacc 2019/05/05 14:25
 */
@Slf4j
public class FreemarkerUtil {

    private FreemarkerUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static Configuration getConfiguration(String basePackagePath) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setLocale(Locale.CHINA);
        cfg.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        cfg.setOutputEncoding("UTF-8");
        cfg.setNumberFormat("#");
        cfg.setClassForTemplateLoading(MailServiceApplication.class, basePackagePath);
        return cfg;
    }

    public static ApiResult<Object> createFileTemplate(Map<String, Object> root, String ftlPackagePath, String templateName, String fileName) {
        final ApiResult<Object> successApiResult = ApiResult.initSuccess();
        final ApiResult<Object> failureApiResult = ApiResult.initFailure();
        try {
            Configuration cfg = FreemarkerUtil.getConfiguration(ftlPackagePath);
            Template template = cfg.getTemplate(templateName, Locale.CHINA);
            final File file = new File(fileName);
            FileUtils.touch(file);
            FileWriterWithEncoding writer = new FileWriterWithEncoding(file, "UTF-8");
            template.process(root, writer);
            writer.close();
            successApiResult.setContent(file);
            return successApiResult;
        } catch (Exception e) {
            log.error("create file template failure!", e);
            failureApiResult.setMessage("create file template failure!");
            failureApiResult.setContent(e.getMessage());
            return failureApiResult;
        }
    }

    public static ApiResult<Object> createStringTemplate(Map<String, Object> root, String ftlPackagePath, String templateName) {
        final ApiResult<Object> successApiResult = ApiResult.initSuccess();
        final ApiResult<Object> failureApiResult = ApiResult.initFailure();
        try {
            Configuration cfg = FreemarkerUtil.getConfiguration(ftlPackagePath);
            Template template = cfg.getTemplate(templateName, Locale.CHINA);
            StringWriter writer = new StringWriter();
            template.process(root, writer);
            writer.close();
            successApiResult.setContent(writer.toString());
            return successApiResult;
        } catch (Exception e) {
            log.error("create string template failure!", e);
            failureApiResult.setMessage("create string template failure!");
            failureApiResult.setContent(e.getMessage());
            return failureApiResult;
        }
    }

    /**
     * 时间戳转为时分秒
     *
     * @param milliseconds 毫秒
     * @return java.lang.String
     * @author isacc 2019/7/11 16:55
     */
    public static String timestamp2String(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hour = seconds / 3600;
        long minute = (seconds - hour * 3600) / 60;
        long second = (seconds - hour * 3600 - minute * 60);

        StringBuilder sb = new StringBuilder();
        if (hour > 0) {
            sb.append(hour).append("h ");
        }
        if (minute > 0) {
            sb.append(minute).append("m ");
        }
        if (second > 0) {
            sb.append(second).append("s");
        }
        if (second == 0) {
            sb.append("<1s");
        }
        return sb.toString();
    }


}
