package org.programmers.cocktail.login.controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.entity.Users;
import org.programmers.cocktail.login.dto.CocktailsDto;
import org.programmers.cocktail.login.dto.UserRegisterDto;
import org.programmers.cocktail.login.service.LoginService;
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private CocktailListsRepository cocktailListsRepository;

    @Autowired
    private CocktailsRepository cocktailsRepository;

    @Autowired
    private LoginService loginService;


    @GetMapping("/register")
    public String showRegisterForm() {
        return "user/register"; // "register.html" 템플릿을 반환
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
        return "user/login"; // "login.html" 템플릿을 반환
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
        System.out.println("email: " + email);
        System.out.println("password: " + password);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //DB에서 이메일, 패스워드 가져오기
        Users users = loginService.findByEmail( email );

        Map<String, Object> response = new HashMap<>();

        int flag = 3;

        //System.out.println("users.getEmail(): " + users.getEmail());
        //System.out.println("users.getPassword(): " + users.getPassword());

        if( users != null ) {

            if (email.equals(users.getEmail()) && encoder.matches(password, users.getPassword())) {
                flag = 0;

                System.out.println("flag0: " + flag);

                // session 부여
                // 이후 세션에 저장된 semail 값은 사용자가 페이지를 이동하더라도 유지되며, 다른 요청에서 접근할 수 있음
                session.setAttribute("semail", email);

                response.put("message", "로그인 성공");

            } else {
                flag = 1;
                System.out.println("flag1: " + flag);
                response.put("message", "이메일 또는 비밀번호가 일치하지 않습니다.");
            }
        } else {
            flag = 2;
            response.put("message", "존재하지 않는 회원입니다.");
        }

        // 모델에 flag 값을 전달
        response.put("flag", flag);

        return ResponseEntity.ok(response);  // JSON 형태로 반환
    }


    // session 확인 후 session 존재하면(=로그인 되어 있으면) mypage로 이동(구현 예정)
    @PostMapping("/logout_ok")
    public String logout_ok(HttpSession session) {

        System.out.println("session.getAttributeBefore: " + session.getAttribute("semail"));

        session.invalidate();

        return "redirect:/api/login"; // JSON 형태로 반환
    }

    @PostMapping("/login_complete")
    public ResponseEntity<Map<String, Object>> login_complete(HttpSession session) {
        System.out.println("login_complete 호출");
        Map<String, Object> response = new HashMap<>();
        int flag = 2;

        if (session.getAttribute("semail") != null ) {
            System.out.println("login_complete semail: " + session.getAttribute("semail"));
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

        return "redirect:/api/login";
    }

    @PostMapping("/mypage_ok")
    public ResponseEntity<Map<String, Object>> myPageOk(
        HttpSession session,
        Model model
        ) {

        System.out.println("mypage_ok 호출");

        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        String email = (String) session.getAttribute("semail");
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
//        for (CocktailsDto c : cocktailsDtos) {
//            System.out.println("CocktailsDto: " + c);
//        }
        Users users = loginService.findByEmail(email);
        if( email != null && users.getId() != null) {

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
            flag = 0;
            //return "user/mypage";
        } else {
            flag = 1;
            response.put("message", "로그인해야 합니다.");
            // redirectAttributes.addFlashAttribute("message", "로그인해야 합니다.");
            //return "redirect:/login";
        }

        response.put("flag", flag);

        return ResponseEntity.ok(response);

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
        System.out.println("modify_ok: " + users.getEmail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        System.out.println("users.getId(): " + users.getId());
        System.out.println("name : " + name );

        int result = loginService.updateUser( name, encodedPassword, users.getId());
        System.out.println("result: " + result );

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
        return "user/withdrawalPage"; // "login.html" 템플릿을 반환
    }

    @GetMapping("/withdrawalCompletePage")
    public String showWithdrawalCompletePage() {
        return "user/withdrawal_complete"; // "login.html" 템플릿을 반환
    }

    @PostMapping("/withdrawal_ok")
    public ResponseEntity<Map<String, Object>> userWithdrawal(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        HttpSession session
    ) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("withdrawal_ok 호출");
        System.out.println("email: " + email);
        System.out.println("password: " + password);
        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        Users users = loginService.findByEmail( email );
        System.out.println("users.getEmail(): " + users.getEmail()); // 잘 나옴
        System.out.println("users.password(): " + users.getPassword()); // 잘 나옴

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

