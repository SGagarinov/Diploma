package com.netology.diploma.service;

import com.netology.diploma.entity.File;
import com.netology.diploma.repository.FileRepository;
import com.netology.diploma.util.TokenStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private FileRepository fileRepository;
    private TokenStorage tokenStorage;

    public FileService(FileRepository fileRepository, TokenStorage tokenStorage) {
        this.fileRepository = fileRepository;
        this.tokenStorage = tokenStorage;
    }

    public void upload(String authToken, String filename, MultipartFile file) throws IOException {
        fileRepository.save(file, getUser(authToken));
    }

    public void delete(String authToken, String filename) {
        fileRepository.delete(filename);
    }

    public File download(String authToken, String filename) {
        return fileRepository.downloadByName(filename);
    }

    public void rename(String filename, String newFilename) {
        fileRepository.rename(filename, newFilename);
    }

    private String getUser(String token) {
        return tokenStorage.getTokenList().get(token);
    }
}
