package co.com.bancolombia.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {

    INVALID_REQUEST("SFB0000", "Invalid request"),
    ACCOUNT_VALIDATION_ERROR("SFB0001", "Account validation error"),
    ACCOUNT_FIND_ERROR("SFB0001", "Account find error"),
    CHANNEL_TRANSACTION_NOT_FOUND("SFB0002", "Channel transaction not found");

    private final String code;
    private final String message;
}
