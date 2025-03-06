package com.example.demo.api;

import com.example.demo.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserApi {

    @Operation(
            summary = "Creare un Utente",
            description = "Crea un nuovo utente nel sistema"
    )
    @PostMapping(produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created. L'utente è stato creato",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Parametri mancanti o non validi",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Token JWT non valido",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - L'email è già esistente",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content()
            )
    })
    ResponseEntity<UserDTO> createUser(
            @Parameter(description = "Oggetto JSON che rappresenta l'utente da creare")
            UserDTO user
    );

    @Operation(summary = "Aggiorna un Utente")
    @PutMapping("/users/{email}")
    ResponseEntity<UserDTO> updateUser(
            @PathVariable String email,
            @RequestBody UserDTO userDTO
    );

    @Operation(summary = "Elimina un Utente")
    @DeleteMapping("/users/{email}")
    ResponseEntity<?> deleteUser(@PathVariable Long id);

    @Operation(summary = "Trova un Utente tramite Email")
    @GetMapping("/users/{email}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id);

    @Operation(summary = "Lista di tutti gli Utenti")
    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUser();
}
