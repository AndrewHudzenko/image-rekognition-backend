package com.correnet.imagerekognition.service.aws.rekognition;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.s3.AmazonS3;
import com.correnet.imagerekognition.service.aws.rekognition.impl.RekognitionClientImpl;
import com.correnet.imagerekognition.service.aws.s3.S3Client;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RekognitionClientTest {
    @Mock
    private AmazonRekognition amazonRekognition;
    @Mock
    private S3Client s3Client;
    @Mock
    private AmazonS3 amazonS3;
    @InjectMocks
    private RekognitionClientImpl rekognitionClient;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        FieldUtils.writeField(rekognitionClient, "bucketName", "correnet-bucket", true);
    }

    /**
     * Method under test {@link RekognitionClientImpl#findAllImagesByObjectName(String)}
     */
    @Test
    void findAllImagesByObjectName() throws MalformedURLException {
        String objectName = "car";

        when(s3Client.getAllImages()).thenReturn(List.of("image1.jpg", "image2.jpg"));
        when(amazonRekognition.detectLabels(any())).thenReturn(new DetectLabelsResult().withLabels(List.of(new Label().withName("car"))));
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL("http://correnet-bucket.s3.amazonaws.com/image1.jpg"));

        List<String> actual = rekognitionClient.findAllImagesByObjectName(objectName);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals("http://correnet-bucket.s3.amazonaws.com/image1.jpg", actual.get(0));
        verify(s3Client).getAllImages();
        verify(amazonRekognition, times(2)).detectLabels(any());
        verify(amazonS3, times(2)).getUrl(anyString(), anyString());
    }
}
