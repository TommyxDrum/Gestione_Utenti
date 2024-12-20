package services;

import com.example.demo.models.UserModel;
import dto.UserDTO;
import mapper.UserMapper;
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
        UserModel user = userRepository.findById(userDTO.getId())
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
}