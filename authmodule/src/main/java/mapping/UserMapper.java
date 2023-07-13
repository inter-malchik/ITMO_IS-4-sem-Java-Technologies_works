package mapping;

import daos.User;
import dto.UserDto;

import java.util.List;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto new_user = new UserDto(user.getId(), user.getUsername(), user.isAdmin(), null);

        if (user.getOwner() != null) {
            new_user.setOwner_id(user.getOwner().getId());
        }

        return new_user;
    }

    public static List<UserDto> toDtosMany(List<User> users) {
        return users.stream().map(UserMapper::toDto).toList();
    }
}
