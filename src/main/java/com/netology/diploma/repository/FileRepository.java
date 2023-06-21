package com.netology.diploma.repository;

import com.netology.diploma.entity.File;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileRepository {
    public void save(MultipartFile file, String user) {

    }

    public void delete(String filename) {

    }

    public File downloadByName(String filename) {
        return null;
    }

    public void rename(String filename, String newFilename) {

    }
}
