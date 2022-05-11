package redcoder.texteditor.exception;

/**
 * Exception indicates user want to execute some operation that is not supported.
 */
public class UnSupportedComponentOperationException extends RuntimeException {

    public UnSupportedComponentOperationException(String message) {
        super(message);
    }
}
