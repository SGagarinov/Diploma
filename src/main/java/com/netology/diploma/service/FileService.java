package com.netology.diploma.service;

import com.netology.diploma.entity.File;
import com.netology.diploma.entity.User;
import com.netology.diploma.repository.FileRepository;
import com.netology.diploma.util.TokenStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;
    private TokenStorage tokenStorage;

    public FileService(FileRepository fileRepository, TokenStorage tokenStorage) {
        this.fileRepository = fileRepository;
        this.tokenStorage = tokenStorage;
    }

    public Boolean upload(String authToken, String filename, MultipartFile file) throws IOException {
        User user = tokenStorage.getTokenList().get(authToken);
        if (user != null)
            return fileRepository.save(file, user.getLogin());
        return null;
    }

    public Boolean delete(String authToken, String filename) {
        User user = tokenStorage.getTokenList().get(authToken);
        if (user != null)
            return fileRepository.delete(filename, user.getLogin());
        return null;
    }

    public File download(String authToken, String filename) {
        User user = tokenStorage.getTokenList().get(authToken);
        if (user != null)
            return fileRepository.downloadByName(filename, user.getLogin());
        return null;
    }

    public Boolean rename(String authToken, String filename, String newFilename) {
        User user = tokenStorage.getTokenList().get(authToken);
        if (user != null)
            return fileRepository.rename(filename, newFilename, user.getLogin());
        return null;
    }

    public List<File> getList(String authToken, int limit) {
        User user = tokenStorage.getTokenList().get(authToken);
        if (user != null)
            return fileRepository.getAllRecords(limit);
        return null;
    }
}
