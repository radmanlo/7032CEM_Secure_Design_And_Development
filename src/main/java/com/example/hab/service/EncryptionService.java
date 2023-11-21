package com.example.hab.service;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionService {
    private static final String SECRET_KEY =
            "1apAHlQvrsvOEvrsGkokyDyF/tm+izJx5iIG1PIbgm7Hyd5FFJgOA+N07ZIgRilq";
    SecretKey secretKey = convertStringToSecretKey(SECRET_KEY);
    public static SecretKey convertStringToSecretKey(String secret) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha256.digest(secret.getBytes());
            byte[] aesKeyBytes = new byte[16];
            System.arraycopy(hashedBytes, 0, aesKeyBytes, 0, aesKeyBytes.length);
            return new SecretKeySpec(aesKeyBytes, "AES");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EncryptionService() {
        try {
            // Generate a new secret key (or load an existing key)
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // AES-256
//            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
