package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Users")
@ToString(exclude = "documentModels") // Evita ricorsione infinita nei toString
@EqualsAndHashCode(exclude = "documentModels") // Evita loop nei hashCode e equals
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utente")
    private Set<DocumentModel> documentModels = new HashSet<>();
}
