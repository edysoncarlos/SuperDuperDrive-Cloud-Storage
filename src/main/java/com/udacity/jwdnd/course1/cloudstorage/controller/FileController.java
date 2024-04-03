package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final UserMapper userMapper;

    @Autowired
    public FileController(FileService fileService, UserMapper userMapper) {
        this.fileService = fileService;
        this.userMapper = userMapper;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, Model model) {

        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);

        if (file.isEmpty()) {
            model.addAttribute("uploadError", "Please select a file to upload.");
            return "redirect:/home";
        }

        if (!fileService.isFilenameAvailable(file.getOriginalFilename())) {
            model.addAttribute("uploadError", "File with the same name already exists. Please choose a different name.");
            return "redirect:/home";
        }

        try {
            fileService.uploadFile(file, user.getUserId());
            model.addAttribute("uploadSuccess", true);
        } catch (IOException e) {
            model.addAttribute("uploadError", "An error occurred while uploading the file. Please try again.");
        }

        return "redirect:/home";
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile(@PathVariable("fileId") Integer fileId, HttpServletResponse response) throws IOException {
        File file = fileService.getFileById(fileId);
        if (file != null) {
            response.setContentType(file.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
            response.getOutputStream().write(file.getFileData());
        }
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId) {
        fileService.deleteFile(fileId);
        return "redirect:/home";
    }
}
