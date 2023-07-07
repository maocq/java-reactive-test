package co.com.bancolombia.model.exceptions;

import co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {
    private final TechnicalErrorMessage errorMessage;

    public TechnicalException(TechnicalErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public TechnicalException(Throwable cause, TechnicalErrorMessage errorMessage) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }
}