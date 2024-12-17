package org.programmers.cocktail.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardUserResponse {

    private Long id;
    private String name;
    private String email;

}
