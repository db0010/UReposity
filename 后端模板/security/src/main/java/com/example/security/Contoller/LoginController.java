package com.example.security.Contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping({"/toIndex","/"})
    public String toIndex(){
        return "index";
    }
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/toLevel1/{id}")
    public String toLevel1(@PathVariable("id") char id){
        return "level1/"+id;
    }
    @RequestMapping("/toLevel2/{id}")
    public String toLevel2(@PathVariable("id") char id){
        return "level2/"+id;
    }
    @RequestMapping("/toLevel3/{id}")
    public String toLevel3(@PathVariable("id") char id){
        return "level3/"+id;
    }
}
