package services;

import dao.OwnerDao;
import dto.CatDto;
import dto.OwnerDto;
import exceptions.OwnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import mappers.CatMapper;
import mappers.OwnerMapper;
import entity.Owner;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtensionMethod({OwnerMapper.class, CatMapper.class})
@RequiredArgsConstructor
public class OwnerService implements IOwnerService {
    private final OwnerDao ownerDao;
    public OwnerDto createOwner(String name, LocalDate birthDate) {
        Owner owner = new Owner(name, birthDate, new ArrayList<>());
        return ownerDao.update(owner).asDto();
    }

    public OwnerDto getOwnerById(int id) {
        return ownerDao.getById(id)
                .orElseThrow(() -> OwnerServiceException.noSuchOwner(id))
                .asDto();
    }

    public List<CatDto> findAllCats(int id) {
        ownerDao.getById(id).orElseThrow(() -> OwnerServiceException.noSuchOwner(id));
        return ownerDao.getAllCats(id).stream().map(CatMapper::asDto).toList();
    }

    public List<OwnerDto> findAllOwners() {
        return ownerDao.getAll().stream().map(OwnerMapper::asDto).toList();
    }

    public void removeOwner(int id) {
        ownerDao.remove(ownerDao.getById(id).orElseThrow(()->OwnerServiceException.noSuchOwner(id)));
    }
}
