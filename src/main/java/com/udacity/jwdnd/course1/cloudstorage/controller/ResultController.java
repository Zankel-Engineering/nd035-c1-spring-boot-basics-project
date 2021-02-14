package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("result")
public class ResultController {

    /** Controller method to serve the login view */
    @GetMapping()
    public String resultView(
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "lastView", required = false) String lastView,
            Model model) {
        model.addAttribute("state", state != null ? state : "initial");
        model.addAttribute("lastView", lastView != null ? lastView : "home");
        return "result";
    }
}
