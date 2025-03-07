package com.example.demo.services;

import com.example.demo.dto.UserDTO;
import com.example.demo.models.UserModel;

import java.util.List;

public interface UserService
{
    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    UserDTO updateUser(String email, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO ConvertToDto(UserModel userModel);
    List<UserDTO> ConvertToDto(List<UserModel> userModel);
    UserModel ConvertToModel(UserDTO userDTO);
}
