package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private EncryptionService encryptionService;
    private CredentialMapper credentialMapper;
    private UserService userService;

    public CredentialService(EncryptionService encryptionService, CredentialMapper credentialMapper, UserService userService) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
        this.userService = userService;
    }

    public List<Credential> getCredentials(String userName) {
        User user = userService.getUser(userName);
        return credentialMapper.getCredentials(user.getUserId());
    }

    public String createCredential(Credential credential, String userName) {
        User user = userService.getUser(userName);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credential.setUserId(user.getUserId());
        return String.valueOf(credentialMapper.insertCredential(credential));
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public Credential getCredential(Integer credentialId) {
        Credential credential = credentialMapper.getCredential(credentialId);
        credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    public String updateCredential(Credential credential, String userName) {
        User user = userService.getUser(userName);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credential.setUserId(user.getUserId());
        return String.valueOf(credentialMapper.updateCredential(credential));
    }
}
