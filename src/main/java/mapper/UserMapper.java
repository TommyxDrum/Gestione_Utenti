package mapper;

import dto.UserDTO;
import models.UserModel;
import org.springframework.stereotype.Service;

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
