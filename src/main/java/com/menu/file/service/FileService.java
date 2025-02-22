package com.menu.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImages(Directory dir, MultipartFile file) {
        validateFileExists(file);
        validateImage(file);
        return uploadFile(dir.getDirectory(), file);
    }

    private String uploadFile(String dir, MultipartFile file) {
        String fileKey = createFilePath(dir, file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(bucket, fileKey, file.getInputStream(), objectMetadata);
        } catch (IOException e) {
            log.error("S3 파일 업로드 실패: {}", e.getMessage());
            throw BaseException.type(FileErrorCode.S3_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(bucket, fileKey).toString();
    }

    private String createFilePath(String dir, String originalFilename) {
        String uuidName = UUID.randomUUID() + "_" + originalFilename;

        return String.format(dir + "/%s", uuidName);
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
