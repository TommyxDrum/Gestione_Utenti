package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Documents")
@ToString(exclude = "utente") // Evita ricorsione infinita nei toString
@EqualsAndHashCode(exclude = "utente") // Evita loop nei hashCode e equals
public class DocumentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nomeFile;

    @Column
    private String tipoFile; //MIME Type

    @Column
    private Long dimensione;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updatedAt")
    private ZonedDateTime updatedAt;

    @Column(name = "downloadedAt")
    private ZonedDateTime downloadedAt;

    @Lob
    private byte dati[]; //Contenuto del file per salvataggio su DB

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private UserModel utente;

    //Eseguito automaticamente da Hibernate prima che un'entità venga salvata nel database per la prima volta
    //Imposta il campo createdAt con la data e ora corrente
    //Assicura che il campo createdAt sia sempre valorizzato in modo automatico
    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    //Deve essere chiamato manualmente dal service o dal controller quando avviene un download.
    public void updateDownloadTime() {
        this.downloadedAt = ZonedDateTime.now();
    }
}
