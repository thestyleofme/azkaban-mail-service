package com.isacc.mail.service;


import com.isacc.mail.api.dto.ApiResult;

/**
 * <p>
 * description
 * </P>
 *
 * @author isacc 2019/05/20 14:02
 */
public interface AzkabanService {

    /**
     * 登录
     *
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 15:48
     */
    ApiResult<Object> login();

    /**
     * 创建项目
     *
     * @param sessionId   sessionId
     * @param name        name
     * @param description description
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 15:48
     */
    ApiResult<Object> createProject(String sessionId, String name, String description);

    /**
     * 上传zip
     *
     * @param sessionId sessionId
     * @param name      name
     * @param zipPath   zipPath
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 15:48
     */
    ApiResult<Object> uploadZip(String sessionId, String name, String zipPath);

    /**
     * 获取正在执行的流
     *
     * @param sessionId   sessionId
     * @param projectName projectName
     * @param flow        flow
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 16:41
     */
    ApiResult<Object> fetchRunningExecutions(String sessionId, String projectName, String flow);

    /**
     * 获取执行流
     *
     * @param sessionId sessionId
     * @param execId execId
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 16:51
     */
    ApiResult<Object> fetchExecFlow(String sessionId, Integer execId);

    /**
     * 获取执行流
     *
     * @param sessionId sessionId
     * @param name      name
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 15:49
     */
    ApiResult<Object> fetchFlows(String sessionId, String name);

    /**
     * 执行流
     *
     * @param sessionId sessionId
     * @param name      name
     * @param flow      flow
     * @return com.isacc.mail.api.dto.ApiResult<java.lang.Object>
     * @author isacc 2019/7/10 15:49
     */
    ApiResult<Object> executeFlow(String sessionId, String name, String flow);

}
