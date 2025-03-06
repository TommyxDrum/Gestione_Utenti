package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO
{
    private Long id;
    private String nomeFile;
    private String tipoFile;
    private Long dimensione;
}
