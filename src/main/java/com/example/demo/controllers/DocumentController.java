package com.example.demo.controllers;

import com.example.demo.services.DocumentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/documents")
public class DocumentController
{
    private final DocumentServiceImpl documentServiceImpl;

    public DocumentController(DocumentServiceImpl documentService)
    {
        this.documentServiceImpl = documentService;
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadDocument(MultipartFile file, )




}

