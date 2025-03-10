package com.example.demo.controllers;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.models.DocumentModel;
import com.example.demo.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/findByUserId/{userId}")
    public ResponseEntity<List<DocumentDTO>> findByUserId(@PathVariable("userId") Long userId)
    {
        List<DocumentDTO> documets = documentService.trovaDocumentiByIdUtente(userId);
        return ResponseEntity.ok(documets);
    }

    @GetMapping("/findDocumentById/{id}")
    public ResponseEntity<DocumentDTO> findDocumentById(@PathVariable("id") Long id)
    {
        DocumentDTO documentDTO = documentService.trovaDocumentoById(id);

        return ResponseEntity.ok(documentDTO);
    }

    @DeleteMapping("/deleteDocument/{id}")
    public ResponseEntity<UserDTO> deleteDocument(@PathVariable("id") Long id)
    {
        documentService.eliminaDocumento(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable("id") Long id)
    {
        DocumentModel documentModel = documentService.downloadDocument(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(documentModel.getTipoFile()));
        headers.setContentDispositionFormData("attachment", documentModel.getNomeFile());
        headers.setContentLength(documentModel.getDimensione());

        return new ResponseEntity<>(documentModel.getDati(), headers, HttpStatus.OK);
    }

    @GetMapping("/downloadAllDocument/{id}")
    public ResponseEntity<Resource> downloadAllDocument(@PathVariable("id") Long id) throws IOException
    {
        //Richiedo il file zip con tutti i documenti dal service
        Resource zipResource = documentService.downloadAllDocumentUser(id);

        //Configurazione degli headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documenti_utente_" + id + ".zip"); //Indica al browser di scaricare il file come allegato e di assegnargli un nome
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Specifica che il contenuto è binario (perfetto per file ZIP)
        headers.setContentLength(zipResource.contentLength()); //Imposta la dimensione del file per aiutare il client a sapere quanto grande sarà il file.

        return ResponseEntity.ok() //Stato 200 ok
                .headers(headers) //Applica gli headers configurati
                .body(zipResource); //Imposta il contenuto del corpo della risposta con il file ZIP
    }
}

