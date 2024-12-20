package mapper;

import com.example.demo.models.UserModel;
import dto.UserDTO;

import org.springframework.stereotype.Service;

//Vediti MapStruct

@Service
public class UserMapper {

    public UserModel UserDTOtoUserModel(UserDTO dto){
        if (dto == null) {
            return null;
        }

        UserModel user = new UserModel();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserDTO UserModelToUserDTO(UserModel model){
        if (model == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(model.getId());
        dto.setEmail(model.getEmail());
        return dto;
    }
}
