package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;

@Controller()
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    /** Controller method to update/create credentials */
    @PostMapping()
    public String addCredential(@ModelAttribute Credential credential, Model model) throws AccountNotFoundException {
       if (credential.getCredentialId() != null) {
            this.credentialService.update(credential);
        } else {
            this.credentialService.create(credential);
        }
        return "redirect:/result?lastView=credential&state=success";
    }

    /** Controller method to delete credentials */
    @GetMapping("/delete")
    public String deleteCredential(@RequestParam("id") String credentialId, Model model) throws AccountNotFoundException {
        this.credentialService.delete(Integer.parseInt(credentialId));
        return "redirect:/result?lastView=credential&state=success";
    }
}
