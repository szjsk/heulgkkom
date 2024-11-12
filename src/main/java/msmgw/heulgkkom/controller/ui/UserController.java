package msmgw.heulgkkom.controller.ui;

import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html 페이지 반환
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/update-project")
    public String showupdateProjectForm() {
        return "update-project";
    }

    @PostMapping("/update-project")
    public String showUpdateProjectForm(@ModelAttribute("projectName") String projectName) {
        userService.updateProjectToUser(projectName);
        return "redirect:/";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("username") String username,
                               @ModelAttribute("password") String password) {
        userService.registerUser(username, password);
        return "redirect:/login"; // 회원가입 완료 후 로그인 페이지로 리다이렉트
    }

}