package services;


import dto.CatDto;
import dto.OwnerDto;

import java.time.LocalDate;
import java.util.List;

public interface IOwnerService {
    OwnerDto createOwner(String name, LocalDate birthDate);
    OwnerDto getOwnerById(int id);
    List<CatDto> findAllCats(int id);
    List<OwnerDto> findAllOwners();
    void removeOwner(int id);
}
