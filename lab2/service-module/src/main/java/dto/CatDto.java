package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;


import java.time.LocalDate;
import java.util.List;

public record CatDto(int id,
                     @NotBlank(message = "Name can`t be empty") String name,
                     @PastOrPresent(message = "Date can`t be in future") LocalDate birthDate,
                     @NotBlank(message = "Breed can`t be empty") String breed,
                     @NotBlank(message = "Colour can`t be empty") String colour,
                     int ownerId,
                     List<Integer> friendsId) {
}
