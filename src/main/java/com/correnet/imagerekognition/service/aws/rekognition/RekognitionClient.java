package com.correnet.imagerekognition.service.aws.rekognition;

import java.util.List;

/**
 * This interface provides methods to interact with the AWS Rekognition service.
 */
public interface RekognitionClient {

    /**
     * This method returns a list of image URLs that contain the specified object in the images.
     *
     * @param objectName The object name to search for in the images.
     * @return A list of image URLs that contain the specified object.
     */
    List<String> findAllImagesByObjectName(String objectName);
}
