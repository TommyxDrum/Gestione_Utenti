package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Documents")
public class DocumentModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nomeFile;

    @Column
    private String tipoFile; //MIME Type

    @Column
    private Long dimensione;

    @Lob
    private byte dati[]; //Contenuto del file per salvataggio su DB

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private UserModel utente;
}
