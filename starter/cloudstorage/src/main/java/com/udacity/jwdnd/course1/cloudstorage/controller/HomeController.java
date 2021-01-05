package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private static final String CUSTOM_ERROR = "customError";
    private static final String RECORDS_EXIST = "Error while saving data: Duplicate records exist";
    private static final String ERROR_WHILE_SAVING_DATA = "Error while saving data";
    private static final String SUCCESS = "success";
    private static final String DELETING = "{} deleting";

    @Value("${maxFileSize}")
    private Long maxFileSize;

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        return generateHomeView(authentication, model);
    }

    private String generateHomeView(Authentication authentication, Model model) {
        String userName = (String) authentication.getPrincipal();
        log.info("{} logged in", userName);
        model.addAttribute("fileUploads", fileService.getFiles(userName));
        model.addAttribute("notes", noteService.getNotes(userName));
        model.addAttribute("credentials", credentialService.getCredentials(userName));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("/file")
    public String uploadFile(Authentication authentication, MultipartFile fileUpload, Model model) {
        String userName = (String) authentication.getPrincipal();
        log.info("File received {}", fileUpload.getOriginalFilename());
        if (fileUpload.isEmpty()) {
            model.addAttribute(CUSTOM_ERROR, "Please select a file!");
            return homeView(authentication, model);
        }
        if (fileUpload.getSize() > maxFileSize) {
            model.addAttribute(CUSTOM_ERROR, "File too large!");
            return homeView(authentication, model);
        }
        try {
            String fileId = fileService.uploadFiles(fileUpload, userName);
            log.info("File created with id {}", fileId);
        } catch (DuplicateKeyException exception) {
            model.addAttribute(CUSTOM_ERROR, RECORDS_EXIST);
            return homeView(authentication, model);
        }  catch (Exception e) {
            log.info(e.getMessage());
            model.addAttribute(CUSTOM_ERROR, ERROR_WHILE_SAVING_DATA);
            return homeView(authentication, model);
        }
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @RequestMapping("/file")
    public ResponseEntity fileDownload(Authentication authentication, Model model, @RequestParam(name = "id") Integer fileId) {
        log.info("{} downloading", fileId);
        File file = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @RequestMapping("/file/delete")
    public String fileDelete(Authentication authentication, Model model, @RequestParam(name = "id") Integer fileId) {
        log.info(DELETING, fileId);
        fileService.deleteFile(fileId);
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @PostMapping("/note")
    public String addNote(Authentication authentication, Note note, Model model) {
        String userName = (String) authentication.getPrincipal();
        try {
            if (StringUtils.isEmpty(note.getNoteId())) {
                String noteId = noteService.createNote(note, userName);
                log.info("Note created {}", noteId);
            } else {
                String noteId = noteService.updateNote(note, userName);
                log.info("Note updated {}", noteId);
            }
        } catch (DuplicateKeyException exception) {
            model.addAttribute(CUSTOM_ERROR, "Error while saving data: Duplicate records exist");
            return homeView(authentication, model);
        }  catch (Exception e) {
            log.info(e.getMessage());
            model.addAttribute(CUSTOM_ERROR, "Error while saving data");
            return homeView(authentication, model);
        }
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @RequestMapping("/note/delete")
    public String noteDelete(Authentication authentication, Model model, @RequestParam(name = "id") Integer noteId) {
        log.info(DELETING, noteId);
        noteService.deleteNote(noteId);
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @RequestMapping("/note")
    public String note(Authentication authentication, Model model, @RequestParam(name = "id") Integer noteId) {
        Note note = noteService.getNote(noteId);
        model.addAttribute("noteTitle", note.getNoteTitle());
        model.addAttribute("noteDescription", note.getNoteDescription());
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @PostMapping("/credential")
    public String addCredential(Authentication authentication, Credential credential, Model model) {
        String userName = (String) authentication.getPrincipal();
        credential.setKey(encryptionService.getKey());
        try {
            if (StringUtils.isEmpty(credential.getCredentialId())) {
                String credentialId = credentialService.createCredential(credential, userName);
                log.info("Credential created {}", credentialId);
            } else {
                String credentialId = credentialService.updateCredential(credential, userName);
                log.info("Credential updated {}", credentialId);
            }
        } catch (DuplicateKeyException exception) {
            log.info(exception.getMessage());
            model.addAttribute(CUSTOM_ERROR, "Error while saving data: Duplicate records exist");
            return homeView(authentication, model);
        } catch (Exception e) {
            log.info(e.getMessage());
            model.addAttribute(CUSTOM_ERROR, "Error while saving data");
            return homeView(authentication, model);
        }
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @RequestMapping("/credential/delete")
    public String credentialDelete(Authentication authentication, Model model, @RequestParam(name = "id") Integer credentialId) {
        log.info(DELETING, credentialId);
        credentialService.deleteCredential(credentialId);
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }

    @RequestMapping("/credential")
    public String credential(Authentication authentication, Model model, @RequestParam(name = "id") Integer credentialId) {
        Credential credential = credentialService.getCredential(credentialId);
        model.addAttribute("credentialURL", credential.getUrl());
        model.addAttribute("credentialUsername", credential.getUsername());
        model.addAttribute("credentialPassword", credential.getPassword());
        model.addAttribute(SUCCESS, true);
        return homeView(authentication, model);
    }
}
