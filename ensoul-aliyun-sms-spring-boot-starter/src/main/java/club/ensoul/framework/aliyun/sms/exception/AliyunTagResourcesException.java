package club.ensoul.framework.aliyun.sms.exception;

/**
 * 自定义异常：阿里云 OSS 操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunTagResourcesException extends AliyunSmsException {

    public AliyunTagResourcesException() {
        super();
    }

    public AliyunTagResourcesException(String message) {
        super(message);
    }

    public AliyunTagResourcesException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunTagResourcesException(Throwable cause) {
        super(cause);
    }

    protected AliyunTagResourcesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
