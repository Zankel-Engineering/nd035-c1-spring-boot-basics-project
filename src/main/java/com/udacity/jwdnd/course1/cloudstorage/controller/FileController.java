package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;

@Controller()
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /** Controller method to add credentials */
    @PostMapping()
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Model model) throws AccountNotFoundException {
        try {
            if (this.fileService.checkExists(file)) {
                model.addAttribute("fileUploadError", true);
                return "redirect:/result?lastView=file&state=duplicate";
            }
            this.fileService.createFileEntry(file);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("fileUploadError", true);
            return "redirect:/result?lastView=file&state=error";
        }
        model.addAttribute("fileUploadError", false);
        return "redirect:/result?lastView=file&state=success";
    }

    /** Controller method to serve a file */
    @GetMapping("/download")
    public ResponseEntity<byte[]> getFile(@RequestParam String name) throws AccountNotFoundException {
        File file = this.fileService.getFile(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    /** Controller method to serve a file */
    @GetMapping("/delete")
    public String deleteFile(@RequestParam String name) throws AccountNotFoundException {
        this.fileService.deleteFile(name);
        return "redirect:/result?lastView=file&state=success";
    }
}
