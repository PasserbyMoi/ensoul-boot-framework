package club.ensoul.framework.aliyun.sms.exception;

/**
 * 自定义异常：阿里云 SMS 操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunSmsException extends RuntimeException {

    public AliyunSmsException() {
        super();
    }

    public AliyunSmsException(String message) {
        super(message);
    }

    public AliyunSmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunSmsException(Throwable cause) {
        super(cause);
    }

    protected AliyunSmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
