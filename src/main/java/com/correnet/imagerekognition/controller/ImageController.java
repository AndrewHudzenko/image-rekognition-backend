package com.correnet.imagerekognition.controller;

import com.correnet.imagerekognition.service.aws.rekognition.impl.RekognitionClientImpl;
import com.correnet.imagerekognition.service.aws.s3.impl.S3ClientImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Image controller", description = "Managing images")

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@CrossOrigin
public class ImageController {
    private final S3ClientImpl s3ClientImpl;
    private final RekognitionClientImpl rekognitionClientImpl;

    @PostMapping("/upload")
    @Operation(summary = "Upload image", description = "Upload image to S3 bucket")
    public String uploadImage(@RequestPart("image") MultipartFile image) {
        log.info("Image Controller: Uploading image: {}", image);
        return s3ClientImpl.uploadFile(image);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete image", description = "Delete image from S3 bucket by URL")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        log.info("Image Controller: Deleting image: {}", fileUrl);
        return this.s3ClientImpl.deleteFile(fileUrl);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all images", description = "Get all images (links) from S3 bucket")
    public List<String> getAllImages() {
        log.info("Image Controller: Getting all images");
        return s3ClientImpl.getAllImages();
    }

    @GetMapping("/recognize")
    @Operation(summary = "Recognize image", description = "Recognize image by object name")
    public List<String> findAllImagesByObjectName(@RequestParam(value = "objectName", required = false) String objectName) {
        log.info("Image Controller: Recognizing image: {}", objectName);
        return rekognitionClientImpl.findAllImagesByObjectName(objectName);
    }

}
