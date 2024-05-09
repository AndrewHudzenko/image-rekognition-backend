package com.correnet.imagerekognition.controller;

import com.correnet.imagerekognition.service.aws.rekognition.impl.RekognitionClientImpl;
import com.correnet.imagerekognition.service.aws.s3.impl.S3ClientImpl;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@CrossOrigin
public class ImageController {
    private final S3ClientImpl s3ClientImpl;
    private final RekognitionClientImpl rekognitionClientImpl;

    // TODO: replace image with file
    @PostMapping("/upload")
    public String uploadImage(@RequestPart("image") MultipartFile image) {
        log.info("Image Controller: Uploading image: {}", image);
        return s3ClientImpl.uploadFile(image);
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        log.info("Image Controller: Deleting image: {}", fileUrl);
        return this.s3ClientImpl.deleteFile(fileUrl);
    }

    @GetMapping("/all")
    public List<String> getAllImages() {
        log.info("Image Controller: Getting all images");
        return s3ClientImpl.getAllImages();
    }

    @GetMapping("/recognize")
    public List<String> findAllImagesByObjectName(@RequestParam(value = "objectName", required = false) String objectName) {
        log.info("Image Controller: Recognizing image: {}", objectName);
        return rekognitionClientImpl.findAllImagesByObjectName(objectName);
    }

}
