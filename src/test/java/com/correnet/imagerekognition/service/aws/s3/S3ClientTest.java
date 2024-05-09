package com.correnet.imagerekognition.service.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.correnet.imagerekognition.service.aws.s3.impl.S3ClientImpl;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3ClientTest {
    @Mock
    private AmazonS3 amazonS3;
    @InjectMocks
    private S3ClientImpl s3Client;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        FieldUtils.writeField(s3Client, "endpointUrl", "https://s3.us-east-1.amazonaws.com", true);
        FieldUtils.writeField(s3Client, "bucketName", "correnet-bucket", true);
    }

    /**
     * Method under test {@link S3ClientImpl#uploadFile(MultipartFile)}
     */
    @Test
    void uploadFile() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        PutObjectResult putObjectResult = new PutObjectResult();

        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(putObjectResult);

        String actual = s3Client.uploadFile(multipartFile);

        assertNotNull(actual);
        verify(amazonS3).putObject(any(PutObjectRequest.class));
    }

    /**
     * Method under test {@link S3ClientImpl#uploadFile(MultipartFile)}
     */
    @Test
    void uploadFile_ThrowsException() {
        MultipartFile multipartFile = null;

        Executable executable = () -> s3Client.uploadFile(multipartFile);
        RuntimeException actual = assertThrows(RuntimeException.class, executable);

        assertNotNull(actual);
        assertEquals("Error while uploading file to S3 bucket", actual.getMessage());
    }

    /**
     * Method under test {@link S3ClientImpl#deleteFile(String)}
     */
    @Test
    void deleteFile() {
        String fileUrl = "https://s3.us-east-1.amazonaws.com/correnet-bucket/test.jpg";

        doNothing().when(amazonS3).deleteObject(any());

        String actual = s3Client.deleteFile(fileUrl);

        assertNotNull(actual);
        assertEquals("Successfully deleted", actual);
        verify(amazonS3).deleteObject(any());
    }

    /**
     * Method under test {@link S3ClientImpl#getAllImages()}
     */
    @Test
    void getAllImages() {
        ObjectListing expected = Instancio.create(ObjectListing.class);

        when(amazonS3.listObjects(anyString())).thenReturn(expected);

        List<String> actual = s3Client.getAllImages();

        assertNotNull(actual);
        assertEquals(expected.getObjectSummaries().size(), actual.size());
        verify(amazonS3).listObjects(anyString());
    }

}
