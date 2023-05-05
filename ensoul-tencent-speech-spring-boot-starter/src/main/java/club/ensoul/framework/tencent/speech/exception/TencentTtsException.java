package club.ensoul.framework.tencent.speech.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常：操作 Topic 异常
 *
 * @author wy_peng_chen6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TencentTtsException extends RuntimeException {

    public TencentTtsException(String message) {
        super(message);
    }

    public TencentTtsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TencentTtsException(Throwable cause) {
        super(cause);
    }

    protected TencentTtsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}