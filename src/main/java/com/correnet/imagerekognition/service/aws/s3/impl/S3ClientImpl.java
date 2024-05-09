package com.correnet.imagerekognition.service.aws.s3.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.correnet.imagerekognition.service.aws.s3.S3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ClientImpl implements S3Client {
    private final AmazonS3 amazonS3;
    @Value("${amazon.endpointUrl}")
    private String endpointUrl;
    @Value("${amazon.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        log.info("S3ClientImpl service: uploading file to S3 bucket");
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            log.error("S3ClientImpl service: error while uploading file to S3 bucket");
            throw new RuntimeException("Error while uploading file to S3 bucket");
        }
        return fileUrl;
    }

    @Override
    public String deleteFile(String fileUrl) {
        log.info("S3ClientImpl service: deleting file from S3 bucket");
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

    @Override
    public List<String> getAllImages() {
        log.info("S3ClientImpl service: getting all images from S3 bucket");
        return amazonS3.listObjects(bucketName).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    private File convertMultiPartToFile(MultipartFile file) {
        log.info("S3ClientImpl service: converting multipart file to file");
        try {
            File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } catch (IOException e) {
            log.error("S3ClientImpl service: error while converting multipart file to file");
            throw new RuntimeException("Error while converting multipart file to file");
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        log.info("S3ClientImpl service: generating file name");
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        log.info("S3ClientImpl service: uploading file to S3 bucket");
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
