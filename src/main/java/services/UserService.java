package services;

import dto.UserDTO;
import mapper.UserMapper;
import models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::UserModelToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO){
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email già esistente");
        }
        UserModel user = new UserModel();
        user.setEmail(userDTO.getEmail());

        UserModel saveUser = userRepository.save(user);
        return userMapper.UserModelToUserDTO(user);
    }

    public UserDTO getUserById(UserDTO userDTO) {
        UserModel user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        return userMapper.UserModelToUserDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO updateUser){
        UserModel existingUser = userRepository.findByEmail(updateUser.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("Utente non trovato"));

        existingUser.setEmail(updateUser.getEmail());

        UserModel saveModel = userRepository.save(existingUser);
        return userMapper.UserModelToUserDTO(existingUser);
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        userRepository.deleteById(id);
    }




    /*

@RestController
@RequestMapping("/api/users") // Indica che tutti gli endpoint di questo controller iniziano con /api/users
public class UserController {

    private final UserService userService;

    @Autowired // Inietta il servizio UserService nel controller
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint per creare un nuovo utente.
     * Accetta un oggetto UserDTO nel corpo della richiesta e lo salva.
     * Restituisce un oggetto UserDTO creato con lo stato HTTP 201 (Created).

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO); // Chiama il metodo di servizio per creare un utente
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); // Restituisce l'utente creato con stato 201
    }


     * Endpoint per ottenere un utente specifico tramite il suo ID.
     * Restituisce l'oggetto UserDTO associato all'ID con stato HTTP 200 (OK).

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id); // Chiama il metodo di servizio per trovare l'utente
        return ResponseEntity.ok(user); // Restituisce l'utente trovato con stato 200
    }


     * Endpoint per ottenere tutti gli utenti.
     * Restituisce una lista di oggetti UserDTO con stato HTTP 200 (OK).

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers(); // Chiama il metodo di servizio per ottenere tutti gli utenti
        return ResponseEntity.ok(users); // Restituisce la lista degli utenti con stato 200
    }

    /**
     * Endpoint per aggiornare un utente esistente.
     * Accetta l'ID dell'utente da aggiornare e i nuovi dati in un oggetto UserDTO.
     * Restituisce l'oggetto UserDTO aggiornato con stato HTTP 200 (OK).

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO); // Chiama il metodo di servizio per aggiornare l'utente
        return ResponseEntity.ok(updatedUser); // Restituisce l'utente aggiornato con stato 200
    }

    /**
     * Endpoint per eliminare un utente esistente.
     * Accetta l'ID dell'utente da eliminare come parametro di percorso.
     * Restituisce una risposta vuota con stato HTTP 204 (No Content).

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); // Chiama il metodo di servizio per eliminare l'utente
        return ResponseEntity.noContent().build(); // Restituisce uno stato 204 senza corpo
    }
}
     */

}