package jpabook.jpashop.exception;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-01-27
 * Time: 12:46
 **/
public class NotEnoughException extends RuntimeException {

    public NotEnoughException() {
        super();
    }

    public NotEnoughException(String message) {
        super(message);
    }

    public NotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
