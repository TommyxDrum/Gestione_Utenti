package com.example.demo.controllers;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/documents")
@Controller
public class DocumentController
{
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService)
    {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestPart MultipartFile file,
            @RequestPart UserDTO userDTO)
    {
        try {
            DocumentDTO documentSaved = documentService.salvaDocumento(file, userDTO);
            return new ResponseEntity<>(documentSaved, HttpStatus.CREATED);
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

