package com.correnet.imagerekognition.service.aws.rekognition.impl;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.correnet.imagerekognition.service.aws.rekognition.RekognitionClient;
import com.correnet.imagerekognition.service.aws.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RekognitionClientImpl implements RekognitionClient {
    private final AmazonRekognition amazonRekognition;
    private final S3Client s3Client;
    private final AmazonS3 amazonS3;
    @Value("${amazon.bucketName}")
    private String bucketName;

    @Override
    public List<String> findAllImagesByObjectName(String objectName) {
        List<String> images = s3Client.getAllImages();

        return images.stream()
                .filter(image -> doLabelsContainObjectName(recognizeImage(image), objectName))
                .map(this::imageNameToImageUrl)
                .toList();
    }

    private List<Label> recognizeImage(String imageName) {

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withName(imageName)
                                .withBucket(bucketName)))
                .withMaxLabels(10)
                .withMinConfidence(75F);

        DetectLabelsResult result = amazonRekognition.detectLabels(request);
        return result.getLabels();
    }

    private boolean doLabelsContainObjectName(List<Label> labels, String objectName) {
        return labels.stream().anyMatch(label -> StringUtils.containsIgnoreCase(label.getName(), objectName));
    }

    private String imageNameToImageUrl(String imageName) {
        return amazonS3.getUrl(bucketName, imageName).toString();
    }

}
