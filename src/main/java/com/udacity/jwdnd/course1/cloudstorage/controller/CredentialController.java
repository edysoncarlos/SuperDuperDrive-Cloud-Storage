package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserMapper userMapper;

    @Autowired
    public CredentialController(CredentialService credentialService, UserMapper userMapper) {
        this.credentialService = credentialService;
        this.userMapper = userMapper;
    }

    @PostMapping("/save")
    public String saveOrUpdateCredential(@ModelAttribute("credentialForm") Credential credential, Authentication authentication, Model model) {

        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        credential.setUserId(user.getUserId());
        int rowsAffected = credentialService.createOrUpdateCredential(credential);
        if (rowsAffected > 0) {
            model.addAttribute("successMessage", "Note saved successfully.");
        } else {
            model.addAttribute("errorMessage", "Failed to save note. Please try again.");
        }

        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication authentication, Model model) {

        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);


        int rowsAffected = credentialService.deleteCredentialById(credentialId, user.getUserId());
        if (rowsAffected > 0) {
            model.addAttribute("successMessage", "Credential deleted successfully.");
        } else {
            model.addAttribute("errorMessage", "Failed to credential note. Please try again.");
        }
        return "redirect:/home";
    }
}
