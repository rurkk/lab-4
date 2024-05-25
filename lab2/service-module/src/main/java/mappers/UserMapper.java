package mappers;

import dto.UserDto;
import entity.User;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class UserMapper {
    public UserDto asDto(User user) {
        Objects.requireNonNull(user);
        return new UserDto(user.getId(),
                user.getName(),
                user.getPassword(),
                user.getRole().toString(),
                user.getOwner().getId());
    }
}