package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public record OwnerDto(int id,
                       @NotBlank(message = "Name can`t be empty") String name,
                       @PastOrPresent(message = "Date can`t be in future") LocalDate birthDate,
                       List<CatDto> cats) {
}
