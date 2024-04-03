package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;

    @Autowired
    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getAllCredentials(Integer userId) {
        return credentialMapper.getAllCredentialsByUserId(userId);
    }

    public int createOrUpdateCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        EncryptionService encryptionService = new EncryptionService();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);

        if (credential.getCredentialId() != null) {
            return credentialMapper.update(credential);
        } else {

            return credentialMapper.insert(credential);
        }
    }

    public int deleteCredentialById(Integer credentialId, Integer userId) {
        return credentialMapper.delete(credentialId, userId);
    }
}
