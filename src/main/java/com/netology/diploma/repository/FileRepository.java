package com.netology.diploma.repository;

import com.netology.diploma.entity.File;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class FileRepository {

    private EntityManager manager;

    public FileRepository(EntityManager manager) {
        this.manager = manager;
    }

    public boolean save(MultipartFile file, String user) throws IOException {
        File saveFile = map(file);
        saveFile.setCreateUser(user);
        manager.persist(saveFile);
        return true;
    }

    public boolean delete(String filename) {

        return true;
    }

    public File downloadByName(String filename) {
        return null;
    }

    public boolean rename(String filename, String newFilename) {
        return true;
    }

    public List<File> getAllRecords(int limit) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<File> query = builder.createQuery(File.class);
        Root<File> root = query.from(File.class);

        query.select(root);

        return manager.createQuery(query)
                .setMaxResults(limit)
                .getResultList();
    }

    private File map(MultipartFile file) throws IOException {
        File result = new File();
        result.setName(file.getOriginalFilename());
        result.setSize(file.getSize());
        result.setCreateDate(LocalDateTime.now());
        result.setType(file.getOriginalFilename().split("\\.")[1]);
        result.setContent(file.getBytes());
        return result;
    }
}
