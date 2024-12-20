package org.programmers.cocktail.repository.authorities;

import java.time.LocalDateTime;

public interface AuthoritiesRepositoryCustom {
    Long countTotalUserUntilYesterday(String authority, LocalDateTime to);
}
