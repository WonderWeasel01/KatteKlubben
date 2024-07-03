package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class medlemmerController {
    

    @GetMapping("/medlemmer")
    public String medlemmer(){
    return "medlemmer";
    }
//

}
