package com.netology.diploma.repository;

import com.netology.diploma.entity.File;
import com.netology.diploma.util.Mapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Repository
@Transactional
public class FileRepository {

    private EntityManager manager;

    public FileRepository(EntityManager manager) {
        this.manager = manager;
    }

    public boolean save(MultipartFile file, String user) throws IOException {
        File saveFile = Mapper.map(file);
        saveFile.setCreateUser(user);
        manager.persist(saveFile);

        return true;
    }

    public boolean delete(String filename, String user) {
        File file = findFileByFilenameAndUser(filename, user);
        manager.remove(file);

        return true;
    }

    public File downloadByName(String filename, String user) {
        return findFileByFilenameAndUser(filename, user);
    }

    public boolean rename(String filename, String newFilename, String user) {
        File file = findFileByFilenameAndUser(filename, user);
        file.setName(newFilename);
        manager.merge(file);

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

    private File findFileByFilenameAndUser(String filename, String user) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<File> query = builder.createQuery(File.class);
        Root<File> root = query.from(File.class);

        query.select(root)
                .where(builder.and(builder.equal(root.get("name"), filename),
                        builder.equal(root.get("createUser"), user)));

        List<File> result = manager.createQuery(query)
                .getResultList();

        if (result.isEmpty())
            throw new RuntimeException("File not found");
        return result.get(0);
    }
}
