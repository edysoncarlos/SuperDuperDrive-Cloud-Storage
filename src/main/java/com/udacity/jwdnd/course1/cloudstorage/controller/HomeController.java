package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserMapper userMapper, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userMapper = userMapper;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String home(Model model) {

        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract the username from the authentication object
        String username = authentication.getName();
        User user = userMapper.getUser(username);

        // Check if the user exists
        if (user != null) {
            Integer userId = user.getUserId();
            // Add user-specific data to the model
            model.addAttribute("files", fileService.getAllFiles(userId));
            model.addAttribute("notes", noteService.getAllNotes(userId));
            model.addAttribute("credentials", credentialService.getAllCredentials(userId));
            model.addAttribute("encryptionService", encryptionService);
        }

        return "home";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}