package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public void createFileEntry(MultipartFile file) throws AccountNotFoundException, IOException {
        int userId = this.userService.getCurrentUserId();
        File internalFile = new File();
        internalFile.setContentType(file.getContentType());
        internalFile.setFileData(file.getBytes());
        internalFile.setFileName(file.getOriginalFilename());
        internalFile.setFileSize(Long.toString(file.getSize()));
        this.fileMapper.insert(internalFile.getFileName(), internalFile.getContentType(), internalFile.getFileSize(), userId, internalFile.getFileData());
    }

    public boolean checkExists(MultipartFile file) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        String fileName = file.getOriginalFilename();
        List<File> files = this.fileMapper.getFiles(userId);
        for (File f : files) {
            if (f.getFileName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public File getFile(String fileName) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        return this.fileMapper.getFile(fileName, userId);
    }

    public void deleteFile(String fileName) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        this.fileMapper.deleteFile(fileName, userId);
    }

    public List<File> getFiles() throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        return this.fileMapper.getFiles(userId);
    }
}
