package com.example.demo.services;

import com.example.demo.dto.UserDTO;

import java.util.List;

public interface UserService
{
    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    UserDTO updateUser(String email, UserDTO userDTO);
    void deleteUser(Long id);
}
