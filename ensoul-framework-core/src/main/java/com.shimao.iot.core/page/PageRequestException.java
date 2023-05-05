package com.shimao.iot.core.page;

/**
 * Add some description about this class.
 *
 * @author striver.cradle
 */
public class PageRequestException extends RuntimeException {
    public PageRequestException() {
        super();
    }

    public PageRequestException(String message) {
        super(message);
    }

    public PageRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageRequestException(Throwable cause) {
        super(cause);
    }

    protected PageRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
