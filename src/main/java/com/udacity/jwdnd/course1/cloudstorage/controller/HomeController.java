package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.login.AccountNotFoundException;

@Controller()
@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;

    @Autowired
    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }


    /** Controller method to serve the home view */
    @GetMapping()
    public String homeView(Model model) throws AccountNotFoundException {
        model.addAttribute("notes", this.noteService.getNotes());
        model.addAttribute("credentials", this.credentialService.getCredentialsEncrypted());
        model.addAttribute("files", this.fileService.getFiles());
        model.addAttribute("credentialService", this.credentialService);
        return "home";
    }
}
