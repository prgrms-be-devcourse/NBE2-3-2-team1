package org.programmers.cocktail.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.programmers.cocktail.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public class RunTimeException extends RuntimeException {
    private final ErrorCode errorCode;
}
