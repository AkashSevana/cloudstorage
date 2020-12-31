package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FileService {

    private FileMapper fileMapper;
    private UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public List<File> getFiles(String userName) {
        User user = userService.getUser(userName);
        log.info("Getting stored files for {}", user.getUserId());
        return fileMapper.getFiles(user.getUserId());
    }

    public String uploadFiles(MultipartFile fileUpload, String userName) throws IOException {
        User user = userService.getUser(userName);
        File file = File.builder()
                .name(fileUpload.getOriginalFilename())
                .contentType(fileUpload.getContentType())
                .data(fileUpload.getBytes())
                .userId(user.getUserId())
                .size(fileUpload.getSize()).build();
        return String.valueOf(fileMapper.insertFile(file));
    }

    public File downloadFile(Integer fileId) {
        return fileMapper.getFile(fileId);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }
}
