package com.growthhub.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.growthhub.file.domain.File;
import com.growthhub.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NCPStorageService {

    private final AmazonS3 amazonS3;

    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile, String path) {
        String name = UUID.randomUUID() + getFileExtension(multipartFile);
        if (path.lastIndexOf("/") != path.length() - 1) {
            path += "/";
        }
        String fileName = path + name;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            String url = amazonS3.getUrl(bucket, fileName).toString();
            File file = File.builder()
                    .name(name)
                    .originalName(multipartFile.getOriginalFilename())
                    .path(path)
                    .url(url)
                    .build();
            fileRepository.save(file);
            return url;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    private String getFileExtension(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            return originalName.substring(originalName.lastIndexOf("."));
        } else {
            return "";
        }
    }
}
