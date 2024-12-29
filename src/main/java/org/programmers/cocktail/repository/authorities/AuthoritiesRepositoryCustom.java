package org.programmers.cocktail.repository.authorities;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthoritiesRepositoryCustom {
    Long countTotalUserUntilYesterday(String authority, LocalDateTime to);

    List<Long> countUserTotalList(LocalDateTime today);
}
