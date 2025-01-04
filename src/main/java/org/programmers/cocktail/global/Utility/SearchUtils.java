package org.programmers.cocktail.global.Utility;

import org.programmers.cocktail.search.dto.UsersTO;
import org.programmers.cocktail.search.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchUtils {

    @Autowired
    private UsersService usersService;

    public UsersTO searchUserByUserEmail(String sessionValue) {
        UsersTO userInfo = usersService.findByEmail(sessionValue);

        if(userInfo==null){
            // 유저 정보 가져올 수 없음(500반환)
            throw new RuntimeException("User not found");
        }

        return userInfo;
    }

}
