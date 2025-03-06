package com.example.demo.services;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.models.DocumentModel;
import com.example.demo.models.UserModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DocumentService
{
    DocumentModel salvaDocumento(MultipartFile file, UserDTO user) throws IOException;
    List<DocumentDTO> trovaDocumentiByIdUtente(Long idUtente);
    DocumentDTO trovaDocumentoById(Long id);
    void eliminaDocumento (Long id);
}
