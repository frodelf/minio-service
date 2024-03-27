package com.example.minioservice.controller.impl;

import com.example.minioservice.service.MinioService;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MinioControllerImplTest {
    @Mock
    private MinioService minioService;
    @InjectMocks
    private MinioControllerImpl minioController;
    @Test
    void deleteImage() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String result = minioController.deleteImage("test.jpg");

        assertEquals("deleted", result);
        verify(minioService).deleteImg("test.jpg");
    }
    @Test
    void save() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "Test Photo".getBytes());
        when(minioService.putMultipartFile(any())).thenReturn("test.jpg");

        String result = minioController.save(file);

        assertEquals("test.jpg", result);
        verify(minioService).putMultipartFile(file);
    }
    @Test
    void getUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        when(minioService.getUrl("test.jpg")).thenReturn("http://example.com/test.jpg");

        String result = minioController.getUrl("test.jpg");

        assertEquals("http://example.com/test.jpg", result);
        verify(minioService).getUrl("test.jpg");
    }
    @Test
    void testGetUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<String> urls = Arrays.asList("test1.jpg", "test2.jpg");
        when(minioService.getUrl(urls)).thenReturn(Arrays.asList("http://example.com/test1.jpg", "http://example.com/test2.jpg"));

        List<String> result = minioController.getUrl(urls);

        assertEquals(2, result.size());
        assertEquals("http://example.com/test1.jpg", result.get(0));
        assertEquals("http://example.com/test2.jpg", result.get(1));
        verify(minioService).getUrl(urls);
    }
}