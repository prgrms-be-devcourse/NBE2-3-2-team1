package org.programmers.cocktail.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.CocktailsDto;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.programmers.cocktail.login.service.LoginService;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private CocktailListsRepository cocktailListsRepository;

    @Autowired
    private CocktailsRepository cocktailsRepository;

    @Autowired
    private LoginService loginService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "user/register";
    }

    // 회원가입
    @PostMapping("/register_ok")
    public ResponseEntity<Map<String, Object>> register_ok(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password
    )
    {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // UserRegisterDto에 사용자입력값 세팅하기
        UserRegisterDto to = new UserRegisterDto();
        to.setEmail( email );
        to.setName( name );
        to.setPassword( encodedPassword );

        Users users = loginService.findByEmail( to.getEmail() );

        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        if ( users != null ) {
            flag = 1;
            response.put("message", "존재하는 아이디입니다.");

        } else {
            flag = 0;
            loginService.insert( to );
            response.put("message", "회원가입이 성공적으로 완료되었습니다.");
        }

        response.put("flag", flag);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }


    // login_ok에서 0을 반환하면 마이페이지로 이동
    // 클라이언트에서 POST /login_ok로 전송된 요청에 포함된
    // 데이터를 @RequestParam으로 받아옴
    @PostMapping("/login_ok")
    public ResponseEntity<Map<String, Object>> login_ok(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession session,
        Model model
    ) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //DB에서 이메일, 패스워드 가져오기
        Users users = loginService.findByEmail( email );

        Map<String, Object> response = new HashMap<>();

        int flag = 3;

        if( users != null ) {

            if (email.equals(users.getEmail()) && encoder.matches(password, users.getPassword())) {
                flag = 0;

                // session 부여
                // 이후 세션에 저장된 semail 값은 사용자가 페이지를 이동하더라도 유지되며, 다른 요청에서 접근할 수 있음
                session.setAttribute("semail", email);

                response.put("message", "로그인 성공");

            } else {
                flag = 1;
                response.put("message", "이메일 또는 비밀번호가 일치하지 않습니다.");
            }
        } else {
            flag = 2;
            response.put("message", "존재하지 않는 회원입니다.");
        }

        response.put("flag", flag);

        return ResponseEntity.ok(response);
    }


    // session 확인 후 session 존재하면(=로그인 되어 있으면) mypage로 이동
    @PostMapping("/logout_ok")
    public String logout_ok(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @PostMapping("/login_complete")
    public ResponseEntity<Map<String, Object>> login_complete(HttpSession session) {
        System.out.println("login_complete 호출");
        Map<String, Object> response = new HashMap<>();
        int flag = 2;

        if (session.getAttribute("semail") != null ) {
            flag = 0;
        } else {
            flag = 1;
            response.put("message", "로그인해야 합니다.");
        }

        response.put("flag", flag);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession session, Model model) {
        System.out.println("mypage 호출");

        String email = (String) session.getAttribute("semail");

        // 세션이 있다면
        if (email != null) {
            Users users = loginService.findByEmail(email);
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

            if (users.getId() != null) {
                // 세션에서 이메일을 통해 DB에서 유저 정보를 가져오기
                // UserRegisterDto 객체에 유저 정보 담기
                UserRegisterDto to = new UserRegisterDto();
                to.setId(users.getId());
                to.setEmail(users.getEmail());
                to.setName(users.getName());
                to.setPassword(users.getPassword());

                // 모델에 유저 정보를 담아서 뷰로 전달
                model.addAttribute("to", to);

                // user, cocktails, cocktaillists 조인해서 user_id타고
                // cocktaillists에 저장된 cocktail_id를 타서 cocktail에 가서 칵테일 정보 가져오기
                List<CocktailsDto> ct = new ArrayList<>();

                for (CocktailLists cl : cocktailLists) {
                    if ( users.getId().equals( cl.getUsers().getId() ) ) {
                        for (CocktailsDto c : cocktailsDtos) {
                            if (cl.getCocktails().getId().equals(c.getId())) {
                                ct.add( c );
                            }
                        }
                    }
                }

                for (CocktailsDto c : ct) {
                    System.out.println("CocktailsDto: " + c);
                }

                model.addAttribute("ct", ct);

                return "user/mypage";
            }
        }

        return "redirect:/login";
    }

    // 이메일은 변경 X
    // 이메일로 유저의 Id를 가져와 비밀번호와 이름을 수정
    @PostMapping("/modify_ok")
    public ResponseEntity<Map<String, Object>> modify_ok(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password,
        HttpSession session
    ) {
        System.out.println("modify_ok 호출");
        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        Users users = loginService.findByEmail( email );

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);


        int result = loginService.updateUser( name, encodedPassword, users.getId());

        if (result > 0) {
            flag = 0;
            session.invalidate();
            response.put("message", "계정 정보 수정 성공");
        } else {
            flag = 1;
            response.put("message", "계정 정보 수정 실패\n로그인 페이지로 돌아갑니다.");
        }


        response.put("flag", flag);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/withdrawalPage")
    public String showWithdrawalPage() {
        return "user/withdrawalPage"; 
    }

    @GetMapping("/withdrawalCompletePage")
    public String showWithdrawalCompletePage() {
        return "user/withdrawal_complete"; 
    }

    @PostMapping("/withdrawal_ok")
    public ResponseEntity<Map<String, Object>> userWithdrawal(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession session
    ) {
        System.out.println("withdrawal_ok 호출");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        Users users = loginService.findByEmail( email );
        System.out.println("users.getEmail(): " + users.getEmail()); 
        System.out.println("users.password(): " + users.getPassword()); 

        int result = 0;
        if(email.equals(users.getEmail()) && encoder.matches(password, users.getPassword())) {
            result = loginService.deleteUser( users.getId() );
            session.invalidate();
        }

        if ( result == 0 ) {
            flag = 1;
            response.put("message", "탈퇴 실패");
        } else {
            flag = 0;
            response.put("message", "탈퇴 성공");
        }

        response.put("flag", flag);

        return ResponseEntity.ok(response);
    }

}

