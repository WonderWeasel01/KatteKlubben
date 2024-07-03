package com.example.demo.Controller;
import com.example.demo.Exceptions.UserAlreadyExistException;
import com.example.demo.Service.AuthenticationService;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.Model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OpretBrugerController {

    @Autowired
    private AuthenticationService as;

    public OpretBrugerController(AuthenticationService authenticationService){
        this.as = authenticationService;
    }


    @GetMapping("/opretBruger")
    public String opretBrugerForm(Model model) {
        model.addAttribute("user", new User());
        return "opretBruger";
    }

    @PostMapping("/opretBruger")
    public String opretBrugerPost(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            as.signUp(user);
            return "index";
        } catch (UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email bruges allerede");
            return "redirect:/opretBruger";
        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Der er sket en fejl. Prøv igen senere");
            e.printStackTrace();
            return "redirect:/opretBruger";
        }
    }
    
}
