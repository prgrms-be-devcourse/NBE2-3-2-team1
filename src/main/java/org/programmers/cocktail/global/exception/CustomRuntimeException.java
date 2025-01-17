package org.programmers.cocktail.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.programmers.cocktail.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class CustomRuntimeException extends java.lang.RuntimeException {
    public CustomRuntimeException(final String message, Throwable cause) {
        super(message, cause);
    }
}
