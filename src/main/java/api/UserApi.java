package api;

import com.example.demo.models.UserModel;
import dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {
    @Operation(
            summary = "Creare un Gruppo",
            description = "Crea un nuovo gruppo nel Tenant di riferimento"
    )
    @PostMapping(produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created. Il gruppo è stato creato",
                    content = @Content(schema = @Schema(implementation = UserModel.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad input parameter - for example missing required headers.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Il Client non è autorizzato",
                    content = @Content(schema = @Schema(implementation = Problem.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Il token JWT presente nell'header X-Auth-Token non è valido.",
                    content = @Content(schema = @Schema(implementation = Problem.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - Il name è già utilizzato all'interno del Tenant",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - An internal server error has occurred. Check the back-end logs for details.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    ResponseEntity<UserModel> createUser(

            @Parameter(description = "Un JSON object che contiene le informazioni del Gruppo.")
            @RequestBody
            @Valid UserDTO user
    );



}
