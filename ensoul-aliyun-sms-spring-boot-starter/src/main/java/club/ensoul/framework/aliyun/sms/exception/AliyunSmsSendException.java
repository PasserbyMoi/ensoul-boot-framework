package club.ensoul.framework.aliyun.sms.exception;

/**
 * 自定义异常：阿里云 OSS 操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunSmsSendException extends AliyunSmsException {

    public AliyunSmsSendException() {
        super();
    }

    public AliyunSmsSendException(String message) {
        super(message);
    }

    public AliyunSmsSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunSmsSendException(Throwable cause) {
        super(cause);
    }

    protected AliyunSmsSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
