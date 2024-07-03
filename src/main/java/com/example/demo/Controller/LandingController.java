package com.example.demo.Controller;

import com.example.demo.Exceptions.EntityNotFoundException;
import com.example.demo.Exceptions.UserAlreadyExistException;
import com.example.demo.Model.Cat;
import com.example.demo.Model.User;
import com.example.demo.Service.AuthenticationService;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.Service.CatWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LandingController
{

    @Autowired
    private AuthenticationService as;

    @Autowired
    private CatWebService cws;

    public LandingController(AuthenticationService authenticationService, CatWebService catWebService){
        this.as = authenticationService;
        this.cws = catWebService;
    }

    @GetMapping("/landing")
    public String getUser(@RequestParam(required = false) String searchQuery, Model model) {
        if(AuthenticationService.user == null){
            return "redirect:/login";
        }
        try {
            List<User> allUsers = cws.getAllUsers(searchQuery);
            List<User> filteredUsers;

            if(searchQuery != null && !searchQuery.isEmpty()) {
                filteredUsers = allUsers.stream()
                        .filter(user -> user.getFirstName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                                user.getLastName().toLowerCase().contains(searchQuery.toLowerCase()))
                        .collect(Collectors.toList());
            } else {
                filteredUsers = allUsers;
            }

            model.addAttribute("users", filteredUsers);
            return "landing";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }


    @GetMapping("/otherUser/{id}")
    public String otherUserForm(@PathVariable("id") int userID, Model model){
        model.addAttribute("user", cws.getUser(userID));

        int catCount = cws.getUserCatCount(userID);
        model.addAttribute("catCount", catCount);

        try{
            List<Cat> catList = cws.getUserCats(userID);
            model.addAttribute("cats", catList);
        } catch(EntityNotFoundException e){
            model.addAttribute("error", "Brugeren har ikke tilføjet et kæledyr endnu");
        }
        return "otherUser";
    }





}
