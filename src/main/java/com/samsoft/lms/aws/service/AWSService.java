package com.samsoft.lms.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.samsoft.lms.aws.dto.DocumentViewResDto;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Service
@Slf4j
public class AWSService {
	@Value("${destinationPath}")
	private String destinationPath;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.secretKey}")
	private String key;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.bucketfilepath}")
	private String bucketFilePath;

	@Autowired
	private AmazonS3 s3Client;

	public String uploadDocumentTos3Bucket() {
		return null;
	}

	public String uploadToS3Bucket(String contentType, String localFilePath) throws Exception {

		String s3BucketPath = "";
		try {
			// Upload a file as a new object with ContentType and title specified.
			PutObjectRequest request = new PutObjectRequest(bucketName, localFilePath.substring(1),
					new File(localFilePath));
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(contentType);
			request.setMetadata(metadata);
			s3Client.putObject(request);
			s3BucketPath = bucketFilePath + localFilePath;
			log.info("s3BucketPath uploaded" + s3BucketPath);
			return s3BucketPath;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : uploadToS3Bucket");
			log.error("Error : " + e);
			throw e;
		}
	}

	private byte[] getDocumentByteArr(String fileUrl) throws Exception {
		byte[] content = null;
		S3ObjectInputStream inputStream = null;
		try {
			if(fileUrl != null) {
				String[] key = fileUrl.split(".com/");
				int len = key.length;
					boolean exist = s3Client.doesObjectExist(bucketName, key[len-1]);
					
					if(!exist) {
						log.info("File not found.");
						return null;
					}
			URI fileToBeDownloaded = new URI(fileUrl);

			AmazonS3URI s3URI = new AmazonS3URI(fileToBeDownloaded);
			log.info("s3Object " + s3URI);
			S3Object s3Object = s3Client.getObject(s3URI.getBucket(), s3URI.getKey());
			log.info("s3Object " + s3Object);
			inputStream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : getDocumentByteArr");
			log.error("Error : " + e);
			throw e;
		}
		return content;
	}

	public Boolean deleteLocalFileAndMkdir(String LocalfolderPath) {
		try {
			// Deleting Local Folder Path
			FileUtils.deleteDirectory(new File(LocalfolderPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;

	}

	public Boolean deleteDocumentToS3Bucket(String filePathUrl) throws Exception {
		try {
			
			if(filePathUrl != null) {
				String[] key = filePathUrl.split(".com/");
				int len = key.length;
				try {
					
					boolean exist = s3Client.doesObjectExist(bucketName, key[len-1]);
					
					if(!exist) {
						log.info("File not found.");
						return Boolean.FALSE;
					}
					log.info("File found.");
					s3Client.deleteObject(bucketName, key[len-1]);
					log.info("File deleted successfully.");
				} catch (AmazonS3Exception e) {
					log.error("deleteDocumentToS3Bucket Amazon Error===>" + e);
					throw e;
				}
			} else {
				return Boolean.FALSE;
			}
		
			return Boolean.TRUE;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : deleteDocumentToS3Bucket");
			log.error("Error : " + e);
			throw e;
		}
	}

	public DocumentViewResDto documentView(String url) throws Exception {

		DocumentViewResDto documentViewResDto = new DocumentViewResDto();
		try {
			String extension = url.substring(url.lastIndexOf(".") + 1);
			log.info("Extension--->" + extension);
			log.info("Url---> "+ url);
			byte[] content = this.getDocumentByteArr(url);
			documentViewResDto.setDocData(content);
			documentViewResDto.setContentType(extension);
			return documentViewResDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: documentView");
			log.error("Request: " + url);
			log.error("Error occurred while Document View" + e);
			throw e;
		}
	}
}
