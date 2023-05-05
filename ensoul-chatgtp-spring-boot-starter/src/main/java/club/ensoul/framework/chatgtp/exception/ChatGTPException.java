package club.ensoul.framework.chatgtp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常：Group 操作异常
 *
 * @author wy_peng_chen6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatGTPException extends RuntimeException {

    public ChatGTPException(String message) {
        super(message);
    }

    public ChatGTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatGTPException(Throwable cause) {
        super(cause);
    }

    protected ChatGTPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}