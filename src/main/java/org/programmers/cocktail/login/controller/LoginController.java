package org.programmers.cocktail.login.controller;

import jakarta.servlet.http.HttpSession;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.programmers.cocktail.login.repository.LoginRepository;
import org.programmers.cocktail.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private LoginService loginService;

    @PostMapping("/login/register")
    public String register(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password)
    {
        // UserRegisterDto에 사용자입력값 세팅하기
        UserRegisterDto to = new UserRegisterDto();
        to.setEmail( email );
        to.setName( name );
        to.setPassword( password );

        //DB에 사용자 입력값 insert하기
        loginService.insert( to );

        return email + name + password + "Register Successful!";
    }


//    @PostMapping("/login")
//    public String login(
//        @RequestParam("email") String email,
//        @RequestParam("password") String password)
//    {
//        System.out.println( "email: " + email );
//
//        Users users = loginService.selectByEmailandPassword( email, password );
//
//        // 이메일, 패스워드 둘 다 일치하지 않으면 에러 남
//        // @Query 쓴 건 dto 쓸 수 없음. users -> userregisterdto 변환할 수 없기 때문
//
//        return users.getName();
//    }


    @RequestMapping("/login_form")
    public String login_form() {
        return "login_form";
    }

    // login_ok에서 0을 반환하면 마이페이지로 이동
    @PostMapping("/login_ok")
    public int login_ok(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession session
    ) {
        // 로그인창에서 입력받은 이메일과 패스워드
        String saveEmail = email;
        String savePassword = password;

        //DB에서 이메일, 패스워드 가져오기
        Users users = loginService.selectByEmailandPassword( email, password );

        int flag = 2;

        if ( saveEmail.equals(users.getEmail()) && savePassword.equals(users.getPassword())) {
            flag = 0;

            // session 부여
            session.setAttribute("semail", email);
            //session.setAttribute("spassword", password);


        } else {
            flag = 1;
        }

        return flag;
    }

    // 이메일, 비밀번호, 이름, 생년월일(아마) select하기 (mypage에 띄워놓기: 프론트)
    @PostMapping("/login/mypage")
    public UserRegisterDto mypage(
        @RequestParam("email") String email
    ) {

        // DB에서 이메일, 패스워드 가져오기
        Users users = loginService.findByEmail( email );

        // cocktail_lists, cocktails 조인해서 특정한 유저의 내가 찜한 칵테일 목록 가져오기(나중에)

        // dto에 users값 넣기
        UserRegisterDto to = new UserRegisterDto();
        to.setId( users.getId() );
        to.setEmail( users.getEmail() );
        to.setPassword( users.getPassword() );
        to.setName( users.getName() );

        // System.out.println(to.getId());
        // System.out.println(to.getEmail());

        return to;
    }


    @PostMapping("/login_complete")
    public int login_complete(HttpSession session) {
        int flag = 2;

        if (session.getAttribute("semail") != null && session.getAttribute("spassword") != null) {
            flag = 0;
        } else {
            flag = 1;
        }

        return flag;
    }

    @PostMapping("/logout_ok")
    public String logout_ok(HttpSession session) {
        // 세션 아이디 파기 후 재생성
        // logout후 login_complete으로 이동 시 세션 아이디가 없어져 flag = 1이 된다.
        session.invalidate();

        return "logout_ok";
    }


}
