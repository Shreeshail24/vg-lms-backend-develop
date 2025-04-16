package com.samsoft.lms.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class EncryptionUtil {
//	@Value("${encrypt_secret_key}")
//	private String SECRET_KEY ="@4FIN!!1212";
	private String SECRET_KEY ="@4FIN!!Sept2021_lms_Backend_@";

//	@Value("${encrypt_sal}")
//	private String SALT="@FIN@!!";
	private String SALT="@FIN@!!Sep2021LMSSALTKEY23052022!!!!!!!!!!!";
	
	
	public  String decrypt(String strToDecrypt) throws Exception {
		try {
			log.info("strToDecrypt ===>" + strToDecrypt);
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
			
			SecretKey tmp = factory.generateSecret(spec);
			
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt.trim())));
		} catch (Exception e) {
			log.error("Error while decrypting: " + e.toString());
			throw e;
		}
	}


	public String encrypt(String strToEncrypt) throws Exception {
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
//			PBKDF2WithHmacSHA1
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			log.error("Error while encrypting: " + e.toString());
			throw e;
		}
	}
	
	public String encBase64(String strToEncrypt) {
//		String encodedString = "This is Base64 encoding and decoding example";
		org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
         
        String encodedVersion = new String(base64.encode(strToEncrypt.getBytes()));
        
        log.info("Encoded Version is " + encodedVersion);
        
        return encodedVersion;
	}
	
	public String decodeBase64(String encodedVersion) {
		org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
		String decodedVersion = new String(base64.decode(encodedVersion.getBytes()));
        
        log.info("Decoded version is "+ decodedVersion);
        
        return decodedVersion;
	}
}
