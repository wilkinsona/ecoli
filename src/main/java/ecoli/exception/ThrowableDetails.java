package ecoli.exception;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author Jonatan Ivanov
 */
@Getter
@ToString
public class ThrowableDetails {
    private String error;
    private String cause;
    private String rootCause;

    public ThrowableDetails(Throwable throwable) {
        Throwable cause = throwable.getCause();
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);

        this.error = throwable.toString();
        this.cause = cause != null ? cause.toString() : null;
        this.rootCause = rootCause != null ? rootCause.toString() : null;
    }
}
