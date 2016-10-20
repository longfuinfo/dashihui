package com.dashihui.afford.common.exception;

/**
 * Created by hhz on 2015/5/18.
 */
public class AffException extends RuntimeException {

    public AffException() {}

    public AffException(String message) {
        super(message);
    }

    public AffException(String discribeStr, Throwable throwable) {
        super(discribeStr, throwable);
    }

    public AffException(Throwable throwable) {
        super(throwable);
    }
}
