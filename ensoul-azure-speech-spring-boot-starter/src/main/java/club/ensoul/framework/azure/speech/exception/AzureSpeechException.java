package club.ensoul.framework.azure.speech.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常：操作 Topic 异常
 *
 * @author wy_peng_chen6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AzureSpeechException extends RuntimeException {

    public AzureSpeechException(String message) {
        super(message);
    }

    public AzureSpeechException(String message, Throwable cause) {
        super(message, cause);
    }

    public AzureSpeechException(Throwable cause) {
        super(cause);
    }

    protected AzureSpeechException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}