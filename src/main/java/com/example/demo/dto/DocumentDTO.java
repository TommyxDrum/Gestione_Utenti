package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO
{
    private Long id;
    @JsonProperty("Nome file")
    private String nomeFile;

    @JsonProperty("Tipo file")
    private String tipoFile;

    @JsonIgnore
    private Long dimensione;

    // Getter personalizzato per restituire la dimensione con unità di misura
    @JsonProperty("Dimensione")
    public String getDimensioneDocumento() {
        return dimensione + " bytes";
    }
}
