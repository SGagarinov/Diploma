package com.netology.diploma.controller;

import com.netology.diploma.entity.File;
import com.netology.diploma.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<?> upload(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestBody MultipartFile file) throws IOException {
        fileService.upload(authToken, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/file")
    public ResponseEntity<?> rename(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestBody Map<String, String> fileNameRequest) {
        fileService.rename(filename, fileNameRequest.get("filename"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> delete(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        fileService.delete(authToken, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> download(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String filename) {
        File file = fileService.download(authToken, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getContent());
    }
}
