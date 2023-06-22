package com.netology.diploma.service;

import com.netology.diploma.dto.files.FileResponse;
import com.netology.diploma.entity.File;

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
}
