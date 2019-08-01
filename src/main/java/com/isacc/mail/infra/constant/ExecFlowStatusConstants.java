package com.isacc.mail.infra.constant;

/**
 * <p>
 * 任务流
 * </p>
 *
 * @author isacc 2019/07/10 16:04
 */
public class ExecFlowStatusConstants {

    private ExecFlowStatusConstants() {
        throw new IllegalStateException("constant class!");
    }

    public static final String FAILED = "FAILED";
    public static final String SUCCEEDED = "SUCCEEDED";
    public static final String KILLED = "KILLED";

}
