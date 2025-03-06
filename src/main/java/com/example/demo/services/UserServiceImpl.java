package com.example.demo.services;

import com.example.demo.models.UserModel;
import com.example.demo.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;


    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    public List<UserDTO> getAllUsers()
    {
        return ConvertToDto(userRepository.findAll());
    }

    public UserDTO createUser(UserDTO userDTO)
    {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
        {
            throw new IllegalArgumentException("Email già esistente");
        }
        UserModel userModel = ConvertToModel(userDTO);//converto in Model
        UserModel userSaved = userRepository.save(userModel);
        UserDTO userDTO1 = ConvertToDto(userSaved);
        return userDTO1;//restituisco il DTO
    }


    public UserDTO getUserById(Long id)
    {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        return ConvertToDto(user);
    }

    public UserDTO updateUser(String email, UserDTO userDTO)
    {
        UserModel existingUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Utente con email: " + email + " non trovato"));

        existingUser.setEmail(userDTO.getEmail());

        userRepository.save(existingUser);

        return ConvertToDto(existingUser);
    }


    public void deleteUser(Long id)
    {
        if (!userRepository.existsById(id))
        {
            throw new IllegalArgumentException("Utente non trovato");
        }
        userRepository.deleteById(id);
    }

    // METODO DI CONVERSIONE

    public UserDTO ConvertToDto(UserModel userModel)
    {
        UserDTO userDTO = null;

        if (userModel != null)
        {
            userDTO = modelMapper.map(userModel, UserDTO.class);
        }
        return userDTO;
    }

    public List<UserDTO> ConvertToDto(List<UserModel> userModel)
    {
        List<UserDTO> userDTO = userModel
                .stream()
                .map(source -> modelMapper.map(source, UserDTO.class))
                .collect(Collectors.toList());
        return userDTO;
    }

    public UserModel ConvertToModel(UserDTO userDTO)
    {
        UserModel userModel = null;

        if (userDTO != null)
        {
            userModel = modelMapper.map(userDTO, UserModel.class);
        }
        return userModel;
    }
}