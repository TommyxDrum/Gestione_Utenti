package controllers;

import api.UserApi;
import com.example.demo.models.UserModel;
import dto.UserDTO;
import mapper.UserMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.UserService;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController implements UserApi {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        UserDTO foundUser = userService.getUserById(userDTO);

        return ResponseEntity.ok(foundUser);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("{/id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO){
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserModel> createUser(UserDTO user) {
            UserDTO createdUser = userService.createUser(user);
            return new ResponseEntity<>(userMapper.UserDTOtoUserModel(createdUser), HttpStatus.CREATED);
    }
}





    

