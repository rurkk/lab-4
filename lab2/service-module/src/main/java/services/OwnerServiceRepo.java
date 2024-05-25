package services;

import dto.CatDto;
import dto.OwnerDto;
import exceptions.CatServiceException;
import exceptions.OwnerServiceException;
import lombok.experimental.ExtensionMethod;
import mappers.CatMapper;
import mappers.OwnerMapper;
import entity.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.IOwnerRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@ExtensionMethod({OwnerMapper.class, CatMapper.class})
public class OwnerServiceRepo implements IOwnerService {

    private final IOwnerRepo ownerRepository;

    @Autowired
    public OwnerServiceRepo(IOwnerRepo ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public OwnerDto createOwner(String name, LocalDate birthDate) {
        Owner owner = new Owner(name, birthDate, new ArrayList<>());
        return ownerRepository.save(owner).asDto();
    }

    @Override
    public OwnerDto getOwnerById(int id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> CatServiceException.noSuchOwner(id))
                .asDto();
    }

    @Override
    public List<CatDto> findAllCats(int id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> OwnerServiceException.noSuchOwner(id));
        return owner.getCatList().stream().map(CatMapper::asDto).toList();

    }

    @Override
    public List<OwnerDto> findAllOwners() {
        return ownerRepository.findAll().stream().map(OwnerMapper::asDto).toList();
    }

    @Override
    public void removeOwner(int id) {
        ownerRepository.deleteById(id);
    }
}
