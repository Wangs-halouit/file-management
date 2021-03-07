package com.halouit.filemanagement.controller;

import com.halouit.filemanagement.dto.FileInfoDTO;
import com.halouit.filemanagement.dto.FileTempInfoDTO;
import com.halouit.filemanagement.service.FileManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileManagementService fileManagementService;

    @PostMapping("/upload")
    public FileInfoDTO upload(@RequestParam("file") MultipartFile file) {
        return fileManagementService.upload(file);
    }

    @GetMapping("/{id}")
    public FileTempInfoDTO getTempId(@PathVariable Long id) {
        return fileManagementService.getTempInfo(id);
    }

    @GetMapping("/temp/{id}")
    public FileSystemResource getFileByTempId(@PathVariable String id, HttpServletResponse response) {
        FileSystemResource file = fileManagementService.getFileByTempId(id);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
        return file;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException {
        return fileManagementService.getTempImage(id);
    }
}
