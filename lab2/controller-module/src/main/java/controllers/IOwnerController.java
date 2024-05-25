package controllers;

import dto.CatDto;
import dto.OwnerDto;

import java.util.List;

public interface IOwnerController {
    OwnerDto createOwner(OwnerDto ownerDto);
    OwnerDto getOwnerById(int id);
    List<CatDto> findAllCats(int id);
    List<OwnerDto> findAllOwners();
    void removeOwner(int id);
}
