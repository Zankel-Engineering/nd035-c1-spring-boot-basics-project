package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.SessionDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    private SessionDTO sessionDTO;

    public SignupController(
            UserService userService,
            SessionDTO sessionDTO) {
        this.userService = userService;
        this.sessionDTO = sessionDTO;
    }

    /** Controller method to serve the signup view */
    @GetMapping()
    public String signupView() {
        return "signup";
    }

    /** Controller method to sign up */
    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {
        if (!this.userService.isUsernameAvailable(user.getUsername())) {
            model.addAttribute("state", "usernameAlreadyExists");
            return "signup";
        }

        int rowsAdded = this.userService.createUser(user);
        if (rowsAdded < 0) {
            model.addAttribute("state", true);
            return "signup";
        }
        sessionDTO.setAttribute("signUp", "Sucessfully signed up. Please log in.");
        return "redirect:/login";
    }
}
