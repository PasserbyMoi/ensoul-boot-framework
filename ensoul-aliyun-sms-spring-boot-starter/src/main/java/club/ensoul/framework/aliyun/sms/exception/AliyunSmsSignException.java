package club.ensoul.framework.aliyun.sms.exception;

/**
 * 自定义异常：阿里云 OSS 操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunSmsSignException extends AliyunSmsException {

    public AliyunSmsSignException() {
        super();
    }

    public AliyunSmsSignException(String message) {
        super(message);
    }

    public AliyunSmsSignException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunSmsSignException(Throwable cause) {
        super(cause);
    }

    protected AliyunSmsSignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
