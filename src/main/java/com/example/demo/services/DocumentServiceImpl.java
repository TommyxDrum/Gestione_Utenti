package com.example.demo.services;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.models.DocumentModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.DocumentRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService
{
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    ModelMapper modelMapper;

    private static final List<String> MIME_TYPES_ACCETTATI = Arrays.asList(
            "application/pdf, image/jped, image/png"
    );

    @Override
    @Transactional
    public DocumentModel salvaDocumento(MultipartFile file, UserDTO user) throws IOException
    {
        String mimeType = file.getContentType();

        if (file.isEmpty())
        {
            throw new IllegalArgumentException("Il file non può essere vuoto");
        }

        if (mimeType == null || !MIME_TYPES_ACCETTATI.contains(mimeType))
        {
            throw new IllegalArgumentException("Tipo di file non supportato");
        }

        UserModel userModel = userServiceImpl.ConvertToModel(user);

        DocumentModel documento = new DocumentModel();
        documento.setNomeFile(file.getOriginalFilename());
        documento.setTipoFile(mimeType);
        documento.setDimensione(file.getSize());
        documento.setDati(file.getBytes());
        documento.setUtente(userModel);

        return documentRepository.save(documento);
    }

    @Override
    public List<DocumentDTO> trovaDocumentiByIdUtente(Long idUtente)
    {
        UserModel userModel = userRepository.findById(idUtente)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        //Restituisce la lista dei documenti per id utente
        List<DocumentModel> documents = documentRepository.findByIdUtente(idUtente);
        if (documents.isEmpty())
        {
            System.out.println("Nessun documento presente per per l'utente con id" + userModel.getId());
        }

        return convertToDto(documents);
    }

    @Override
    public DocumentDTO trovaDocumentoById(Long id)
    {
        DocumentModel documentModel = documentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Documento non trovato"));
        return convertToDto(documentModel);
    }

    @Override
    public void eliminaDocumento(Long id)
    {
        DocumentModel documentModel = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        documentRepository.deleteById(id);
    }

    //Metodi di conversione

    private DocumentDTO convertToDto(DocumentModel documentModel)
    {
        DocumentDTO documentDTO = null;
        if (documentModel != null)
        {
            documentDTO = modelMapper.map(documentModel, DocumentDTO.class);
        }
        return documentDTO;
    }

    private DocumentModel convertToModel(DocumentDTO documentDTO)
    {
        DocumentModel documentModel = null;
        if (documentDTO != null)
        {
            documentModel = modelMapper.map(documentDTO, DocumentModel.class);
        }
        return documentModel;
    }

    private List<DocumentDTO> convertToDto(List<DocumentModel> documentModel)
    {
        List<DocumentDTO> documentDTO = documentModel
                .stream()
                .map(source -> modelMapper.map(source, DocumentDTO.class))
                .toList();
        return documentDTO;
    }
}