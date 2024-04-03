package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserMapper userMapper;

    @Autowired
    public NoteController(NoteService noteService, UserMapper userMapper) {
        this.noteService = noteService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String getNotes(Authentication authentication, Model model) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        model.addAttribute("notes", noteService.getAllNotes(user.getUserId()));
        return "notes";
    }

    @PostMapping("/addOrUpdate")
    public String addOrUpdateNote(@ModelAttribute("noteForm") Note note, Authentication authentication, Model model) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);

        note.setUserId(user.getUserId());

        int rowsAffected = noteService.createOrUpdateNote(note);
        if (rowsAffected > 0) {
            model.addAttribute("successMessage", "Note saved successfully.");
        } else {
            model.addAttribute("errorMessage", "Failed to save note. Please try again.");
        }
        return "redirect:/home";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNoteById(@PathVariable("noteId") Integer noteId, Authentication authentication, Model model) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);

        int rowsAffected = noteService.deleteNoteById(noteId, user.getUserId());
        if (rowsAffected > 0) {
            model.addAttribute("successMessage", "Note deleted successfully.");
        } else {
            model.addAttribute("errorMessage", "Failed to delete note. Please try again.");
        }
        return "redirect:/home";
    }
}
