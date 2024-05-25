package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

public record UserDto(int id,
                      @NotBlank(message = "Username can`t be empty") String name,
                      @NotBlank(message = "Password can`t be empty") String password,
                      @NotBlank(message = "Role can`t be empty") String role,
                      int ownerId) {
}
