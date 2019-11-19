package exceptions;

public class MyEntityIllegalArgumentException extends Exception {
    public MyEntityIllegalArgumentException() {
    }

    public MyEntityIllegalArgumentException(String message) {
        super(message);
    }

    public MyEntityIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyEntityIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public MyEntityIllegalArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
