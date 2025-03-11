package com.example.demo.services;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.models.DocumentModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.DocumentRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    //Orari permessi per il download
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(18, 0);

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private static final List<String> MIME_TYPES_ACCETTATI = List.of("application/pdf", "image/jpeg", "image/png", "application/octet-stream");
    private static final long MAX_FILE_SIZE = (5 * 1024 * 1024); //5Mb

    @Override
    @Transactional
    public DocumentDTO salvaDocumento(MultipartFile file, UserDTO user) throws IOException {
        String mimeType = file.getContentType();

        //Validazioni dei file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Il file non può essere vuoto");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Il file è troppo grande (MAX 5Mb)");
        }

        if (mimeType == null || !MIME_TYPES_ACCETTATI.contains(mimeType)) {
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

        //Aggiorno l'orario del download
        documento.updateDownloadTime();

        //Salvo il documento sul DB
        documentRepository.save(documento);

        //Converto e restituisco il DTO al controller
        return convertToDto(documento);
    }

    @Override
    @Transactional()
    public List<DocumentDTO> trovaDocumentiByIdUtente(Long idUtente) {
        //Cerchiamo l'utente
        UserModel userModel = userRepository.findById(idUtente)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        //Restituisce la lista dei documenti per id utente
        List<DocumentModel> documents = documentRepository.findByIdUtente(idUtente);

        //Se non trovo documenti restituisco una lista vuota
        if (documents.isEmpty()) {
            logger.info("Nessun documento presente per l'utente con id" + userModel.getId());

            return Collections.emptyList();
        }

        return convertToDto(documents);
    }

    @Override
    public DocumentDTO trovaDocumentoById(Long id) {
        DocumentModel documentModel = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Documento non trovato"));

        return convertToDto(documentModel);
    }

    @Override
    public void eliminaDocumento(Long id) {
        DocumentModel documentModel = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        documentRepository.deleteById(id);
    }

    @Transactional
    public DocumentModel downloadDocument(Long id) {
        //Controllo se il dwnload è consentito nella fascia di orario
        if (!isDownloadAllowed()) {
            throw new IllegalStateException("Download non consentito a questo orario");
        }
        //Verifichiamo la presenza del file
        DocumentModel documentModel = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File non trovato"));

        return documentModel;
    }

    @Transactional
    public Resource downloadAllDocumentUser(Long id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        //Recupeo dei documenti dell'utente
        Set<DocumentModel> documentiUtente = userModel.getDocumentModels();

        if (documentiUtente.isEmpty()) {
            throw new IllegalArgumentException("Nessun documento trovato");
        }

        //Preparazione del file ZIP
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); //Serve per scrivere i dati in memoria come array di byte. È un buffer che conterrà il file ZIP finale.
             ZipOutputStream zos = new ZipOutputStream(baos)) //Serve per scrivere i dati in formato ZIP dentro il ByteArrayOutputStream
        {
            for (DocumentModel documentModel : documentiUtente) //Ciclo sui documenti
            {
                ZipEntry zipEntry = new ZipEntry(documentModel.getNomeFile()); //Creo una nuova "voce" all'interno dello zip
                zos.putNextEntry(zipEntry); //Indica l'inizio della scrittura di un nuovo file nello  zip
                zos.write(documentModel.getDati()); // Scrive effettivamente i file nel file zip
                zos.closeEntry(); // Chiude la voce corrente nel file zip e prepara il flusso di un nuovo inserimento (se necessario)
            }

            zos.finish(); // FInalizza il file zip, assicurandosi che tutte le voci siano scritte correttamente
            return new ByteArrayResource //Converte tutto il contenuto del ByteArrayOutputStream in un array di byte
                    (baos.toByteArray()); //Incapsula l'array di byte per restituirlo in modo sicuro e gestibile come Resource. Questo è utile per il controller, che lo gestirà nella risposta HTTP.

        } catch (IOException e) {
            throw new RuntimeException("Errore nella creazione del file zip" + e);
        }
    }

    //Metodi di conversione
    @Override
    public DocumentDTO convertToDto(DocumentModel documentModel) {
        DocumentDTO documentDTO = null;
        if (documentModel != null) {
            documentDTO = modelMapper.map(documentModel, DocumentDTO.class);
        }
        return documentDTO;
    }

    @Override
    public DocumentModel convertToModel(DocumentDTO documentDTO) {
        DocumentModel documentModel = null;
        if (documentDTO != null) {
            documentModel = modelMapper.map(documentDTO, DocumentModel.class);
        }
        return documentModel;
    }

    @Override
    public List<DocumentDTO> convertToDto(List<DocumentModel> documentModel) {
        List<DocumentDTO> documentDTO = documentModel
                .stream()
                .map(source -> modelMapper.map(source, DocumentDTO.class))
                .toList();
        return documentDTO;
    }

    // COntrollo fasce orarie consentite
    @Override
    public boolean isDownloadAllowed() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        LocalTime currentTime = now.toLocalTime();

        return !currentTime.isBefore(START_TIME) && !currentTime.isAfter(END_TIME);
    }
}