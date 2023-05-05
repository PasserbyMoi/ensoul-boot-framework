package club.ensoul.framework.aliyun.sms.exception;

/**
 * 自定义异常：阿里云 OSS 操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunShortUrlException extends AliyunSmsException {

    public AliyunShortUrlException() {
        super();
    }

    public AliyunShortUrlException(String message) {
        super(message);
    }

    public AliyunShortUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunShortUrlException(Throwable cause) {
        super(cause);
    }

    protected AliyunShortUrlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
