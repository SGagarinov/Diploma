package com.netology.diploma.util;

import com.netology.diploma.dto.files.FileResponse;
import com.netology.diploma.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static List<FileResponse> map(List<File> files) {
        List<FileResponse> response = new ArrayList<>();
        for (File file: files) {
            response.add(new FileResponse(file.getName(), file.getSize()));
        }
        return response;
    }

    public static File map(MultipartFile file) throws IOException {
        File result = new File();
        result.setName(file.getOriginalFilename());
        result.setSize(file.getSize());
        result.setCreateDate(LocalDateTime.now());
        result.setType(file.getOriginalFilename().split("\\.")[1]);
        result.setContent(file.getBytes());
        return result;
    }
}
