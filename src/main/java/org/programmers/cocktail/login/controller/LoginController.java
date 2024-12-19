package org.programmers.cocktail.login.controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.CocktailsDto;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.programmers.cocktail.login.service.LoginService;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private CocktailListsRepository cocktailListsRepository;

    @Autowired
    private CocktailsRepository cocktailsRepository;

    @Autowired
    private LoginService loginService;

    // 회원가입
    @PostMapping("/login/register")
    public int register(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password)
    {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // UserRegisterDto에 사용자입력값 세팅하기
        UserRegisterDto to = new UserRegisterDto();
        to.setEmail( email );
        to.setName( name );
        to.setPassword( encodedPassword );

        //DB에 사용자 입력값 insert하기
        int flag = loginService.insert( to );

        return flag;
    }

    // 이메일, 비밀번호, 이름, 생년월일(아마) select하기 (mypage에 띄워놓기: 프론트)
    @PostMapping("/login/mypageUserInformation")
    public UserRegisterDto mypageUserInformation(
        @RequestParam("email") String email
    ) {

        // DB에서 이메일, 패스워드 가져오기
        Users users = loginService.findByEmail( email );

        // dto에 users값 넣기
        UserRegisterDto to = new UserRegisterDto();
        to.setId( users.getId() );
        to.setEmail( users.getEmail() );
        to.setName( users.getName() );
        to.setPassword( users.getPassword() );

        return to;
    }

    // Mypage에서 내가 찜한 칵테일 보여주기
    @PostMapping("/login/mypageUsersFavoriteCocktail")
    public List<CocktailsDto> mypageUsersFavoriteCocktail(
        @RequestParam("email") String email
    ) {

        Users users = loginService.findByEmail( email );
        List<CocktailLists> cocktailLists = cocktailListsRepository.findAll();
        List<Cocktails> cocktails = cocktailsRepository.findAll();


        List<CocktailsDto> cocktailsDtos = new ArrayList<>();

        for ( Cocktails cocktail : cocktails ) {
            CocktailsDto cocktailsTo = new CocktailsDto();
            cocktailsTo.setId(cocktail.getId());
            cocktailsTo.setName(cocktail.getName());
            cocktailsTo.setImage_url(cocktail.getImage_url());
            cocktailsDtos.add(cocktailsTo);
        }

        // 칵테일 정보 잘 가져오는지 확인
        for (CocktailsDto c : cocktailsDtos) {
            System.out.println("CocktailsDto: " + c);
        }

        List<CocktailsDto> ct = new ArrayList<>();

        // user, cocktails, cocktaillists 조인해서 user_id타고
        // cocktaillists에 저장된 cocktail_id를 타서 cocktail에 가서 칵테일 정보 가져오기
        for (CocktailLists cl : cocktailLists) {
            if ( users.getId().equals( cl.getUsers().getId() ) ) {
                for (CocktailsDto c : cocktailsDtos) {
                    if (cl.getCocktails().getId().equals(c.getId())) {
                        ct.add( c );
                    }
                }
            }
        }

        return ct;
    }


    // login_ok에서 0을 반환하면 마이페이지로 이동
    // 클라이언트에서 POST /login_ok로 전송된 요청에 포함된
    // 데이터를 @RequestParam으로 받아옴
    @PostMapping("/login_ok")
    public int login_ok(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession session
    ) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 로그인창에서 입력받은 이메일과 패스워드
        String saveEmail = email;
        String savePassword = password;

        //DB에서 이메일, 패스워드 가져오기
        Users users = loginService.findByEmail( email );

        int flag = 2;

        if ( saveEmail.equals(users.getEmail()) && encoder.matches(savePassword, users.getPassword())) {
            flag = 0;

            // session 부여
            // 이후 세션에 저장된 semail 값은 사용자가 페이지를 이동하더라도 유지되며, 다른 요청에서 접근할 수 있음
            session.setAttribute("semail", email);

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

        // dto에 users값 넣기
        UserRegisterDto to = new UserRegisterDto();
        to.setId( users.getId() );
        to.setEmail( users.getEmail() );
        to.setPassword( users.getPassword() );
        to.setName( users.getName() );


        return to;
    }

    // session 확인 후 session 존재하면(=로그인 되어 있으면) mypage로 이동(구현 예정)
    @PostMapping("/login_complete")
    public int login_complete(HttpSession session) {
        int flag = 2;

        if (session.getAttribute("semail") != null) {
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

    // 회원 정보 수정 페이지 / 이름, 이메일, 생년월일(나중에), 전화번호(나중에) form에 넣어놓기
    @PostMapping("/login/modify")
    public UserRegisterDto userModify(HttpSession session) {
        // 세션에서 "semail"이라는 키로 저장된 객체를 반환
        // 출력: 유저 이메일
        String email = (String) session.getAttribute("semail");
        Users users = loginService.findByEmail( email );

        UserRegisterDto to = new UserRegisterDto();
        to.setId( users.getId() );
        to.setEmail( users.getEmail() );
        to.setName( users.getName() );

        return to;
    }

    // 이메일은 변경 X
    // 이메일로 유저의 Id를 가져와 비밀번호와 이름을 수정
    @PostMapping("/login/modify_ok")
    public int modify_ok(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password
    ) {
        int flag = 0;

        Users users = loginService.findByEmail( email );

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        flag = loginService.updateUser( name, encodedPassword, users.getId());


        return flag;
    }


    @PostMapping("/withdrawal_ok")
    public int userWithdrawal(
        @RequestParam("email") String email,
        @RequestParam("password") String password
    ) {
        int flag = 0;

        Users users = loginService.selectByEmailandPassword( email, password );
        // System.out.println("users.getEmail(): " + users.getEmail()); // 잘 나옴

        int result = 0;
        if(email.equals(users.getEmail()) && password.equals(users.getPassword())) {
            result = loginService.deleteUser( users.getId() );
        }

        if ( result == 0 ) {
            flag = 1;
        } else {
            flag = 0;
        }

        return flag;
    }

}

