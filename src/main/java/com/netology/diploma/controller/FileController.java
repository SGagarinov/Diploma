package com.netology.diploma.controller;

import com.netology.diploma.dto.files.FileResponse;
import com.netology.diploma.entity.File;
import com.netology.diploma.service.FileService;
import com.netology.diploma.service.Mapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<?> upload(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestBody MultipartFile file) throws IOException {
        try {
            if (authToken.isEmpty())
                new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
            if (filename.isEmpty())
                return new ResponseEntity<>("Error input data", HttpStatus.BAD_REQUEST);

            return fileService.upload(authToken, filename, file)
                    ? ResponseEntity.ok("Success upload")
                    : new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>("Error input data", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/file")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> rename(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestBody Map<String, String> fileNameRequest) {
        try {
            if (authToken.isEmpty())
                new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
            if (filename.isEmpty())
                return new ResponseEntity<>("Error input data", HttpStatus.BAD_REQUEST);

            return fileService.rename(authToken, filename, fileNameRequest.get("filename"))
                    ? ResponseEntity.ok("Success rename")
                    : new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>("Error upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> delete(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        try {
            if (authToken.isEmpty())
                new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
            if (filename.isEmpty())
                return new ResponseEntity<>("Error input data", HttpStatus.BAD_REQUEST);

            return fileService.delete(authToken, filename) ?
                    ResponseEntity.ok("Success deleted")
                    : new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>("Error delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<?> download(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        try {
            if (authToken.isEmpty())
                new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
            if (filename.isEmpty())
                return new ResponseEntity<>("Error input data", HttpStatus.BAD_REQUEST);

            File file = fileService.download(authToken, filename);
            return file != null
                    ? ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getContent())
                    : new ResponseEntity<>("Unauthorized error", HttpStatus.UNAUTHORIZED);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>("Error download file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<FileResponse>> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
        try {
            if (authToken.isEmpty())
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized error");
            if (limit == 0)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Error input data");

            if (fileService.getList(authToken, limit) != null)
                return ResponseEntity.ok(Mapper.map(fileService.getList(authToken, limit)));
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized error");
        }
        catch (RuntimeException ex) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting file list");
        }
    }
}
