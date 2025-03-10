package com.example.demo.repositories;

import com.example.demo.models.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentModel, Long>
{
    Optional<DocumentModel> findById(Long id);

    void deleteById(Long id);

    List<DocumentModel> findByNomeFileOrderByDimensione(String nomeFile);

    //SQL puro
    @Query(value = " SELECT A.id, A.dati, A.dimensione, A.nome_file, A.tipo_file, A.id_user FROM documents A JOIN users B ON A.id_user = B.id WHERE B.id = :id ORDER BY A.nome_file", nativeQuery = true)
    List<DocumentModel> findByIdUtente(@Param("id") Long id);

}
