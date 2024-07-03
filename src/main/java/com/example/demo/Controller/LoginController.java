package com.example.demo.Controller;
import com.example.demo.Exceptions.EntityNotFoundException;
import com.example.demo.Model.User;
import com.example.demo.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    @Autowired
    private AuthenticationService as;

    public LoginController(AuthenticationService authenticationService){
        this.as = authenticationService;
    }

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        try{
            as.login(user.getEmail(),user.getPassword());
            return "redirect:/landing";
        } catch(EntityNotFoundException e){
            redirectAttributes.addFlashAttribute("error", "Email eller password forkert.");
            return "redirect:/login";
        }
    }
}
