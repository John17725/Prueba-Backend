package com.logistic.orders.service.impl;


import com.logistic.orders.config.UserInitializer;
import com.logistic.orders.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.UUID;
import software.amazon.awssdk.core.sync.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

    public S3ServiceImpl(S3Client s3Client, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        logger.info("Uploading file: {} ",file.getOriginalFilename());
        try {
            String key = folder + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            String urlFormated =  String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
            logger.debug("Uploaded file: {} with url: {} ",key, urlFormated);
            return urlFormated;
        } catch (Exception e) {
            logger.error("Error while uploading file with message: {}",e.getMessage());
            throw new RuntimeException("Error al subir archivo a S3", e);
        }
    }
}