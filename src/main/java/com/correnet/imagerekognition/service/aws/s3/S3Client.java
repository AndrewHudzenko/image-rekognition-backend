package com.correnet.imagerekognition.service.aws.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * This interface provides methods to interact with the AWS S3 service.
 */
public interface S3Client {

    /**
     * This method returns the URL of the uploaded file.
     *
     * @param multipartFile The file to upload.
     * @return The URL of the uploaded file.
     */
    String uploadFile(MultipartFile multipartFile);

    /**
     * This method deletes the file with the specified URL.
     *
     * @param fileUrl The URL of the file to delete.
     * @return A message indicating whether the file was deleted successfully.
     */
    String deleteFile(String fileUrl);

    /**
     * This method returns a list of URLs of all the images in the S3 bucket.
     *
     * @return A list of URLs of all the images in the S3 bucket.
     */
    List<String> getAllImages();

}
