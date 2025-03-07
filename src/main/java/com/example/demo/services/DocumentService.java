package com.example.demo.services;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.models.DocumentModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService
{
    DocumentDTO salvaDocumento(MultipartFile file, UserDTO user) throws IOException;
    List<DocumentDTO> trovaDocumentiByIdUtente(Long idUtente);
    DocumentDTO trovaDocumentoById(Long id);
    void eliminaDocumento (Long id);
    DocumentDTO convertToDto(DocumentModel documentModel);
    DocumentModel convertToModel(DocumentDTO documentDTO);
    List<DocumentDTO> convertToDto(List<DocumentModel> documentModel);
}
