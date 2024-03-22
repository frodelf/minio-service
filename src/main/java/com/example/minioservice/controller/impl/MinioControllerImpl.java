package com.example.minioservice.controller.impl;

import com.example.minioservice.controller.MinioController;
import com.example.minioservice.service.MinioService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/minio")
public class MinioControllerImpl implements MinioController {
    private final MinioService minioService;
    @DeleteMapping("/delete/{image}")
    public String deleteImage(@PathVariable String image) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioService.deleteImg(image);
        return "deleted";
    }
    @PostMapping("/save")
    public String save(@RequestParam MultipartFile image) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioService.putMultipartFile(image);
    }
    @GetMapping("/get")
    public String getUrl(@RequestParam String url) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioService.getUrl(url);
    }
    @GetMapping("/get/list")
    public List<String> getUrl(@RequestParam List<String> urls) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioService.getUrl(urls);
    }
}