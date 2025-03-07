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
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService
{
    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private static final List<String> MIME_TYPES_ACCETTATI = List.of("application/pdf", "image/jpeg", "image/png", "application/octet-stream");
    private static final long MAX_FILE_SIZE = (5*1024*1024); //5Mb

    @Override
    @Transactional
    public DocumentDTO salvaDocumento(MultipartFile file, UserDTO user) throws IOException
    {
        String mimeType = file.getContentType();

        //Validazioni dei file
        if (file.isEmpty())
        {
            throw new IllegalArgumentException("Il file non può essere vuoto");
        }

        if (file.getSize() > MAX_FILE_SIZE)
        {
            throw new IllegalArgumentException("Il file è troppo grande (MAX 5Mb)");
        }

        if (mimeType == null || !MIME_TYPES_ACCETTATI.contains(mimeType))
        {
            throw new IllegalArgumentException("Tipo di file non supportato");
        }

        //L'utente deve essere presente nel DB per poter assocuare un documento
        UserModel userModel = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        //Creo il nuovo documento
        DocumentModel documento = new DocumentModel();
        documento.setNomeFile(file.getOriginalFilename());
        documento.setTipoFile(mimeType);
        documento.setDimensione(file.getSize());
        documento.setDati(file.getBytes());
        documento.setUtente(userModel);

        //Associo il documento all'utente
        userModel.getDocumentModels().add(documento);

        //Salvo il documento sul DB
        documentRepository.save(documento);

        //Converto e restituisco il DTO al controller
        return convertToDto(documento);
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
    @Override
    public DocumentDTO convertToDto(DocumentModel documentModel)
    {
        DocumentDTO documentDTO = null;
        if (documentModel != null)
        {
            documentDTO = modelMapper.map(documentModel, DocumentDTO.class);
        }
        return documentDTO;
    }

    @Override
    public DocumentModel convertToModel(DocumentDTO documentDTO)
    {
        DocumentModel documentModel = null;
        if (documentDTO != null)
        {
            documentModel = modelMapper.map(documentDTO, DocumentModel.class);
        }
        return documentModel;
    }
    @Override
    public List<DocumentDTO> convertToDto(List<DocumentModel> documentModel)
    {
        List<DocumentDTO> documentDTO = documentModel
                .stream()
                .map(source -> modelMapper.map(source, DocumentDTO.class))
                .toList();
        return documentDTO;
    }
}