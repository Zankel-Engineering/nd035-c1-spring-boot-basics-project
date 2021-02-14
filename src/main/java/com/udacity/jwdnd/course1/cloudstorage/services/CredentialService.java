package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.security.auth.kerberos.EncryptionKey;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    public static final String ENCRYPTION_KEY = RandomStringUtils.random(16, true, true);;

    private final UserService userService;
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(UserService userService, CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public String decryptPassword(String password) {
        if (password == null) {
            return "";
        }
        return this.encryptionService.decryptValue(password, ENCRYPTION_KEY);
    }

    public void create(Credential credential) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        credential.setPassword(this.encryptionService.encryptValue(credential.getPassword(), ENCRYPTION_KEY));
        this.credentialMapper.insert(credential.getUrl(), credential.getUsername(), credential.getPassword(), userId);
    }

    public void update(Credential credential) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        this.credentialMapper.update(
                credential.getCredentialId(),
                credential.getUrl(),
                credential.getUsername(),
                this.encryptionService.encryptValue(credential.getPassword(), ENCRYPTION_KEY),
                userId
        );
    }

    public void delete(int credentialId) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        this.credentialMapper.delete(credentialId, userId);
    }

    public List<Credential> getCredentialsEncrypted() throws AccountNotFoundException {
        List<Credential> credentialList = this.getCredentials();
        return credentialList;
    }

    public Credential getCredentialById(int credentialId) throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        return this.credentialMapper.getCredentialById(userId, credentialId);
    }

    private List<Credential> getCredentials() throws AccountNotFoundException {
        int userId = this.userService.getCurrentUserId();
        return this.credentialMapper.getCredentials(userId);
    }
}
