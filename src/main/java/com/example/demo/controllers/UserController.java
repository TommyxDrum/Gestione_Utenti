package com.example.demo.controllers;

import com.example.demo.api.UserApi;
import com.example.demo.dto.UserDTO;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements UserApi {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        UserDTO foundUser = userService.getUserById(id);

        return ResponseEntity.ok(foundUser);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(email, userDTO);
        return ResponseEntity.ok(updatedUser);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        UserDTO userSaved =  userService.createUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }
}





    

