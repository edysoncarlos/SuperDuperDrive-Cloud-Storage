package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles(Integer userId) {
        return fileMapper.getAllFilesById(userId);
    }

    public boolean isFilenameAvailable(String filename) {
        return fileMapper.getFileByName(filename) == null;
    }

    public void uploadFile(MultipartFile file, int userId) throws IOException {
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        newFile.setFileData(file.getBytes());
        newFile.setUserId(userId);
        fileMapper.insert(newFile);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }
}
