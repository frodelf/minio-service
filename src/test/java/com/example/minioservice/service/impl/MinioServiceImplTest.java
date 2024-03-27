package com.example.minioservice.service.impl;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MinioServiceImplTest {
    @Mock
    private MinioClient minioClient;
    @InjectMocks
    private MinioServiceImpl minioService;
    @Test
    void getPhoto() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String objectName = "test.jpg";
        byte[] expectedBytes = "Test Photo".getBytes();
        InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(new GetObjectResponse(null, null, null, null, inputStream));

        byte[] result = minioService.getPhoto(objectName);

        assertArrayEquals(expectedBytes, result);
        verify(minioClient).getObject(any(GetObjectArgs.class));
    }
    @Test
    void deleteImg() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String objectName = "test.txt";

        minioService.deleteImg(objectName);

        verify(minioClient).removeObject(any());
    }
    @Test
    void putMultipartFile() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        minioService.putMultipartFile(multipartFile);

        verify(minioClient).putObject(any());
    }
    @Test
    void putMultipartFileWithException() throws Exception {
        MockMultipartFile multipartFile = Mockito.mock(MockMultipartFile.class);

        doThrow(IOException.class).when(multipartFile).transferTo(any(File.class));

        assertThrows(IOException.class, () -> minioService.putMultipartFile(multipartFile));
    }
    @Test
    void getUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "test.jpg";
        byte[] photoBytes = "Test Photo".getBytes();
        String expectedUrl = "data:image/jpeg;base64, " + java.util.Base64.getEncoder().encodeToString(photoBytes);
        InputStream inputStream = new ByteArrayInputStream(photoBytes);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(new GetObjectResponse(null, null, null, null, inputStream));

        String result = minioService.getUrl(fileName);

        assertEquals(expectedUrl, result);
        verify(minioClient).getObject(any(GetObjectArgs.class));
    }

    @Test
    void GetAllUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<String> fileNames = new ArrayList<>();
        fileNames.add("test1.jpg");
        fileNames.add("test2.jpg");
        byte[] photoBytes = "Test Photo".getBytes();
        String expectedUrl = "data:image/jpeg;base64, " + java.util.Base64.getEncoder().encodeToString(photoBytes);
        InputStream inputStream = new ByteArrayInputStream(photoBytes);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(new GetObjectResponse(null, null, null, null, inputStream));

        List<String> result = minioService.getUrl(fileNames);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUrl, result.get(0));
        verify(minioClient, times(2)).getObject(any(GetObjectArgs.class));
    }
}