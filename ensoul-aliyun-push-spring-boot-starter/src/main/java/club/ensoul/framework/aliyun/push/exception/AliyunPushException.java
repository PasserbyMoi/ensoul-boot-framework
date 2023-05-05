package club.ensoul.framework.aliyun.push.exception;

/**
 * 自定义异常：阿里云推送操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunPushException extends RuntimeException {

    public AliyunPushException() {
        super();
    }

    public AliyunPushException(String message) {
        super(message);
    }

    public AliyunPushException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunPushException(Throwable cause) {
        super(cause);
    }

    protected AliyunPushException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
