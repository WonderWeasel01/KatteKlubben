package com.example.demo.Controller;

import com.example.demo.Model.Cat;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.CatWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OpretKat {

    @Autowired
    private AuthenticationService as;

    @Autowired
    private CatWebService cws;

    public OpretKat(AuthenticationService authenticationService, CatWebService catWebService){
        this.as = authenticationService;
        this.cws = catWebService;
    }
    @GetMapping("/opretKat")
    public String createCatform(Model model){
        if(AuthenticationService.user == null){
            return "redirect:/login";
        }
        model.addAttribute("cat", new Cat());
        return "opretKat";
    }

    @PostMapping("/opretKat")
    public String createCatPost(@ModelAttribute Cat cat, RedirectAttributes redirectAttributes){
        try{
            cat.setOwnerID(AuthenticationService.user.getId());
            cws.createCat(cat);
            redirectAttributes.addFlashAttribute("successMessage", "Kat oprettet!");
            return "redirect:/dinBruger";
        } catch (DataAccessException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Der er sket en fejl. Prøv igen senere");
            e.printStackTrace();
            return "redirect:/opretKat";
        } catch(NullPointerException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage","You need to be logged in");
            return "redirect:/opretKat";
        }
    }
}
