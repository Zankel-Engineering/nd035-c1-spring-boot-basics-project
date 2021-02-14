package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.SessionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    private SessionDTO sessionDTO;

    public LoginController(SessionDTO sessionDTO) {
        this.sessionDTO = sessionDTO;
    }

    /** Controller method to serve the login view */
    @GetMapping()
    public String loginView(@RequestParam(value = "state", required = false) String state, Model model) {
        String signUp = sessionDTO.getValue("signUp");
        String loginFailed = sessionDTO.getValue("loginFailed");
        if (signUp != null) {
            model.addAttribute("state", "registrationSuccess");
            sessionDTO.setAttribute("signupSuccess", null);
        } else if(loginFailed != null) {
            model.addAttribute("state", "loginFailed");
            sessionDTO.setAttribute("loginFailed", null);
        } else {
            model.addAttribute("state", state != null ? state : "initial");
        }
        return "login";
    }
}
