package club.ensoul.framework.aliyun.sms.exception;

/**
 * 自定义异常：阿里云 OSS 操作失败
 *
 * @author wy_peng_chen6
 */
public class AliyunSmsTemplateException extends AliyunSmsException {

    public AliyunSmsTemplateException() {
        super();
    }

    public AliyunSmsTemplateException(String message) {
        super(message);
    }

    public AliyunSmsTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliyunSmsTemplateException(Throwable cause) {
        super(cause);
    }

    protected AliyunSmsTemplateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
