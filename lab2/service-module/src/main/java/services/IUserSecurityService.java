package services;

import dto.UserDto;

public interface IUserSecurityService {
    UserDto createUser(String name, String password, String roleName, int ownerId);
}
