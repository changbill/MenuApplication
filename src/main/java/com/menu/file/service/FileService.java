package com.menu.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.menu.file.config.AwsCredentialsProperties;
import com.menu.file.domain.Directory;
import com.menu.file.exception.FileErrorCode;
import com.menu.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.menu.file.exception.FileErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3 amazonS3;
    private final AwsCredentialsProperties awsCredentialsProperties;

    public String uploadImages(Directory dir, MultipartFile file) {
        validateFileExists(file);
        validateImage(file);
        return uploadFile(dir.getDirectory(), file);
    }

    public void deleteFiles(String photoUrl) {
        if(photoUrl == null || photoUrl.isEmpty()) {
            throw BaseException.type(INVALID_DIRECTORY);
        }
        String fileKey = photoUrl.substring(57);
        try {
            amazonS3.deleteObject(getBucket(), fileKey);
        } catch (AmazonS3Exception e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

    private String uploadFile(String dir, MultipartFile file) {
        String fileKey = createFilePath(dir, file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(getBucket(), fileKey, file.getInputStream(), objectMetadata);
        } catch (IOException e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage());
            throw BaseException.type(FileErrorCode.S3_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(getBucket(), fileKey).toString();
    }

    private String createFilePath(String dir, String originalFilename) {
        String uuidName = UUID.randomUUID() + "_" + originalFilename;

        return String.format(dir + "/%s", uuidName);
    }

    private String getBucket() {
        return awsCredentialsProperties.getS3().getBucket();
    }

    private static void validateFileExists(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw BaseException.type(EMPTY_FILE);
        }
    }

    private static void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw BaseException.type(NOT_IMAGE);
        }
    }
}
