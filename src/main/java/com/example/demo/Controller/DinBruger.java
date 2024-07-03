package com.example.demo.Controller;

import com.example.demo.Exceptions.EntityNotFoundException;
import com.example.demo.Model.Cat;
import com.example.demo.Model.User;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.CatWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DinBruger {

    @Autowired
    private CatWebService cws;

    @Autowired
    private AuthenticationService as;

    public DinBruger(CatWebService catWebService, AuthenticationService authenticationService){
        this.cws = catWebService;
        this.as = authenticationService;
    }

    @GetMapping("/dinBruger")
    public String dinBruger(Model model) {
        if(AuthenticationService.user == null){
            return "redirect:/login";
        }
        model.addAttribute("user", AuthenticationService.user);

        int catCount = cws.getUserCatCount(AuthenticationService.user.getId());
        model.addAttribute("catCount", catCount);

        try{
            List<Cat> catList = cws.getUserCats(AuthenticationService.user.getId());
            model.addAttribute("cats", catList);
        } catch(EntityNotFoundException e){
            model.addAttribute("error", "Du har ikke tilføjet et kæledyr endnu");
        }

        return "dinBruger";
    }

    @GetMapping("/editUserInfo/{id}")
    public String editUserInfoForm(@PathVariable("id") int userID, Model model){
        if(AuthenticationService.user == null || AuthenticationService.user.getId() != userID){
            return "redirect:/landing";
        }

        model.addAttribute("user", AuthenticationService.user);
        return "editUserInfo";
    }
    @PostMapping("/editUserInfo")
    public String editUserInfoPost(@ModelAttribute User user){
        cws.updateUser(user);
        return "redirect:/dinBruger";
    }

    @GetMapping("/editCatInfo/{catId}")
    public String editCatInfoForm(@PathVariable("catId") int catId, Model model){
        if (AuthenticationService.user == null) {
        //if (AuthenticationService.user == null || !cws.userOwnsCat(AuthenticationService.user.getId(), catId)) {
            return "redirect:/landing";
        }

        Cat cat = cws.findCatById(catId);
        if (cat == null) {
            return "redirect:/landing";
        }

        model.addAttribute("cat", cat);
        System.out.println(cat);
        return "editCatInfo";
    }


    @PostMapping("/editCatInfo")
    public String editCatInfoPost(@ModelAttribute Cat cat){
        if (cat == null) {
            return "redirect:/error";
        }
        System.out.println(cat);
        cws.updateCat(cat);
        return "redirect:/dinBruger";
    }

    @GetMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable("id") int userID, Model model){
        if(AuthenticationService.user == null || AuthenticationService.user.getId() != userID){
            return "redirect:/login";
        }

        if(as.deleteUser(userID)){
            as.logOut();
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "Der skete en fejl.");
            return "dinBruger";
        }
    }
    @GetMapping("/deleteCat/{id}")
    public String deleteCat(@PathVariable("id") int catId, Model model){
        if(AuthenticationService.user == null){
            return "redirect:/login";
        }

        // Check if the logged-in user owns the cat
        if(!cws.userOwnsCat(AuthenticationService.user.getId(), catId)){
            return "redirect:/dinBruger"; // Redirect to user profile if the user doesn't own the cat
        }

        // Attempt to delete the cat
        boolean deleted = cws.deleteCat(catId);
        if(deleted){
            return "redirect:/dinBruger"; // Redirect to user profile after successful deletion
        } else {
            model.addAttribute("errorMessage", "Could not delete the cat."); // Add error message if deletion fails
            return "dinBruger";
        }
    }








}
