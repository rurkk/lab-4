package controllers;

import dto.UserDto;

public interface IUserController {
    public UserDto createUser(UserDto userDTO);
}
