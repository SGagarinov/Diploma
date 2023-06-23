package com.netology.diploma.service;

import com.netology.diploma.entity.File;
import com.netology.diploma.entity.User;
import com.netology.diploma.repository.FileRepository;
import com.netology.diploma.util.TokenStorage;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;
    @Mock
    private TokenStorage tokenStorage;
    @Mock
    private FileRepository fileRepository;

    private String token = UUID.randomUUID().toString();
    private HashMap<String, User> map = new HashMap<>();
    private File file = new File();
    User user = new User("admin");

    @BeforeEach
    void init() {
        map.put(token, user);

        file.setName("my filename.tmp");
        file.setCreateUser(user.getLogin());
        file.setType("tmp");
        file.setSize(10L);

        when(tokenStorage.getTokenList()).thenReturn(map);
    }

    @Test
    void getFiles() {
        when(fileRepository.getAllRecords(3)).thenReturn(List.of(file));
        List<File> result = fileService.getList(token, 3);
        assertNotNull(result);
        assertEquals(result.get(0).getName(), file.getName());
        assertEquals(result.get(0).getCreateUser(), file.getCreateUser());
        assertEquals(result.get(0).getType(), file.getType());
        assertEquals(result.get(0).getSize(), file.getSize());
    }

    @Test
    void upload() throws IOException {
        byte[] content = "test".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), content);

        fileService.upload(token, file.getName(), multipartFile);

        verify(fileRepository, times(1)).save(multipartFile, user.getLogin());
    }

    @Test
    void delete() {
        fileService.delete(token, file.getName());
        verify(fileRepository, times(1)).delete(file.getName(), user.getLogin());
    }

    @Test
    void download() {
        file.setContent("test".getBytes());
        when(fileRepository.downloadByName(anyString(), anyString())).thenReturn(file);

        File downloadFile = fileService.download(token, file.getName());

        assertEquals(file.getName(), downloadFile.getName());
    }

    @Test
    void rename() {
        fileService.rename(token, file.getName(), "new file name");

        verify(fileRepository, times(1)).rename(file.getName(), "new file name", user.getLogin());
    }
}
