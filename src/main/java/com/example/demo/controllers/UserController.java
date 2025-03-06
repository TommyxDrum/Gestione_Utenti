package com.example.demo.controllers;

import com.example.demo.api.UserApi;
import com.example.demo.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements UserApi {


    @Autowired
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        UserDTO foundUser = userServiceImpl.getUserById(id);

        return ResponseEntity.ok(foundUser);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<UserDTO> users = userServiceImpl.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userServiceImpl.updateUser(email, userDTO);
        return ResponseEntity.ok(updatedUser);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        UserDTO userSaved =  userServiceImpl.createUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }
}





    

