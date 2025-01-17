package org.programmers.cocktail.global.exception;

import java.lang.RuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.programmers.cocktail.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class CustomDataAccessException extends RuntimeException {
    public CustomDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
