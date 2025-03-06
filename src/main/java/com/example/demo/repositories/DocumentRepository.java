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
    List<DocumentModel> findByNomeFileOrderByDimensione(String nomeFile);

    //SQL puro
    @Query(value = "SELECT A.nome_file FROM Documents A JOIN USERS B ON A.ID_USER = B.ID WHERE B.ID = :id ORDER BY B.nome", nativeQuery = true)
    List<DocumentModel>findByIdUtente(@Param("id") Long id);

    @Query(value = "DELETE FROM Documents WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
