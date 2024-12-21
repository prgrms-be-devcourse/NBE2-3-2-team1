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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
//@RequestMapping("/api/login")
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
    public String register_ok(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password,
        RedirectAttributes redirectAttributes )
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

        redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다!");

        return "redirect:/login";
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

        // 로그인창에서 입력받은 이메일과 패스워드
        String saveEmail = email;
        String savePassword = password;

        //DB에서 이메일, 패스워드 가져오기
        Users users = loginService.findByEmail( email );

        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        System.out.println("users.getEmail(): " + users.getEmail());
        System.out.println("users.getPassword(): " + users.getPassword());

        if ( saveEmail.equals(users.getEmail()) && encoder.matches(savePassword, users.getPassword())) {
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

        // 모델에 flag 값을 전달
        response.put("flag", flag);

        return ResponseEntity.ok(response);  // JSON 형태로 반환
    }


    // session 확인 후 session 존재하면(=로그인 되어 있으면) mypage로 이동(구현 예정)
    @PostMapping("/logout_ok")
    public String logout_ok(HttpSession session) {

        System.out.println("session.getAttributeBefore: " + session.getAttribute("semail"));

        session.invalidate();

        return "redirect:/login"; // JSON 형태로 반환
    }

    @PostMapping("/login_complete")
    public ResponseEntity<Map<String, Object>> login_complete(HttpSession session) {

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
    public String showMyPage(
        HttpSession session,
        Model model,
        RedirectAttributes redirectAttributes) {

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
        for (CocktailsDto c : cocktailsDtos) {
            System.out.println("CocktailsDto: " + c);
        }

        if( email != null ) {
            // 세션에서 이메일을 통해 DB에서 유저 정보를 가져오기
            Users users = loginService.findByEmail(email);

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

            model.addAttribute("ct", ct);

            return "user/mypage";
        } else {
            redirectAttributes.addFlashAttribute("message", "로그인해야 합니다.");
            return "redirect:/login";
        }
    }

    // 이메일은 변경 X
    // 이메일로 유저의 Id를 가져와 비밀번호와 이름을 수정
    @PostMapping("/modify_ok")
    public ResponseEntity<Map<String, Object>> modify_ok(
        @RequestParam("email") String email,
        @RequestParam("name") String name,
        @RequestParam("password") String password
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

        if (result == 0) {
            flag = 0;
            response.put("message", "계정 정보 수정 성공");
        } else {
            flag = 1;
            response.put("message", "계정 정보 수정 실패\n로그인 페이지로 돌아갑니다.");
        }


        response.put("flag", flag);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdrawal_ok")
    public ResponseEntity<Map<String, Object>> userWithdrawal(
        @RequestParam("email") String email,
        @RequestParam("password") String password
    ) {
        System.out.println("withdrawal_ok 호출");
        Map<String, Object> response = new HashMap<>();

        int flag = 2;

        Users users = loginService.selectByEmailandPassword( email, password );
        // System.out.println("users.getEmail(): " + users.getEmail()); // 잘 나옴

        int result = 0;
        if(email.equals(users.getEmail()) && password.equals(users.getPassword())) {
            result = loginService.deleteUser( users.getId() );
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


//    // Mypage에서 내가 찜한 칵테일 보여주기
//    @PostMapping("/login/mypageUsersFavoriteCocktail")
//    public List<CocktailsDto> mypageUsersFavoriteCocktail(
//        @RequestParam("email") String email
//    ) {
//
//        Users users = loginService.findByEmail( email );
//        List<CocktailLists> cocktailLists = cocktailListsRepository.findAll();
//        List<Cocktails> cocktails = cocktailsRepository.findAll();
//
//
//        List<CocktailsDto> cocktailsDtos = new ArrayList<>();
//
//        for ( Cocktails cocktail : cocktails ) {
//            CocktailsDto cocktailsTo = new CocktailsDto();
//            cocktailsTo.setId(cocktail.getId());
//            cocktailsTo.setName(cocktail.getName());
//            cocktailsTo.setImage_url(cocktail.getImage_url());
//            cocktailsDtos.add(cocktailsTo);
//        }
//
//        // 칵테일 정보 잘 가져오는지 확인
//        for (CocktailsDto c : cocktailsDtos) {
//            out.println("CocktailsDto: " + c);
//        }
//
//        List<CocktailsDto> ct = new ArrayList<>();
//
//        // user, cocktails, cocktaillists 조인해서 user_id타고
//        // cocktaillists에 저장된 cocktail_id를 타서 cocktail에 가서 칵테일 정보 가져오기
//        for (CocktailLists cl : cocktailLists) {
//            if ( users.getId().equals( cl.getUsers().getId() ) ) {
//                for (CocktailsDto c : cocktailsDtos) {
//                    if (cl.getCocktails().getId().equals(c.getId())) {
//                        ct.add( c );
//                    }
//                }
//            }
//        }
//
//        return ct;
//    }

    // 이메일, 비밀번호, 이름, 생년월일(아마) select하기 (mypage에 띄워놓기: 프론트)
//    @PostMapping("/login/mypageUserInformation")
//    public void mypageUserInformation(
//        @RequestParam("email") String email,
//        Model model
//    ) {
//
//        // DB에서 이메일, 패스워드 가져오기
//        Users users = loginService.findByEmail( email );
//
//        // dto에 users값 넣기
//        UserRegisterDto to = new UserRegisterDto();
//        to.setId( users.getId() );
//        to.setEmail( users.getEmail() );
//        to.setName( users.getName() );
//        to.setPassword( users.getPassword() );
//
//        model.addAttribute("to", to);
//
//    }



}

