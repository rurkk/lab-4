package controllers;

import dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import services.UserSecurityService;


@RestController
@RequestMapping("/user/")
public class UserController implements IUserController {

    private final UserSecurityService userService;

    public UserController(UserSecurityService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @Override
    public UserDto createUser(@Valid @RequestBody UserDto userDTO) {
        return userService.createUser(userDTO.name(), userDTO.password(), userDTO.role(), userDTO.ownerId());
    }

    @PostMapping("create")
    public UserDto createUser(@RequestParam(name = "name") String name,
                            @RequestParam(name = "password") String password,
                            @RequestParam(name = "role") String role,
                            @RequestParam(name = "ownerId") int ownerId) {
        return userService.createUser(name, password, role, ownerId);
    }
}
