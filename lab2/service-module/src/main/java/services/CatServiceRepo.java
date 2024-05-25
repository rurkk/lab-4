package services;

import dto.CatDto;
import entity.User;
import exceptions.CatServiceException;
import lombok.experimental.ExtensionMethod;
import mappers.CatMapper;
import models.Breed;
import entity.Cat;
import models.Colour;
import entity.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repositories.ICatRepo;
import repositories.IOwnerRepo;
import repositories.IUserRepo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtensionMethod(CatMapper.class)
@Service
public class CatServiceRepo implements ICatService {

    private final ICatRepo catRepository;
    private final IOwnerRepo ownerRepository;

    private final IUserRepo userRepository;

    @Autowired
    public CatServiceRepo(ICatRepo catRepository, IOwnerRepo ownerRepository, IUserRepo userRepository) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CatDto createCat(String name, LocalDate birthDate, String breedName, String colourName, int ownerId) {
        if (!ownerRepository.existsById(ownerId)) {
            throw CatServiceException.noSuchOwner(ownerId);
        }

        Breed breed = Breed.findByValue(breedName);
        Colour colour = Colour.findByValue(colourName);

        Owner tmpOwner = ownerRepository.getReferenceById(ownerId);
        Cat tmpCat = new Cat(name, birthDate, breed, colour, tmpOwner, new HashSet<>());
        tmpOwner.addCat(tmpCat);
        return catRepository.save(tmpCat).asDto();

    }

    @Override
    public void makeFriends(int catId1, int catId2) {
        Cat cat1 = catRepository.findById(catId1)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId1));
        Cat cat2 = catRepository.findById(catId2)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId2));
        cat1.addFriend(cat2);
        cat2.addFriend(cat1);
        catRepository.saveAll(List.of(cat1, cat2));
    }

    @Override
    public void unfriendCats(int catId1, int catId2) {
        Cat cat1 = catRepository.findById(catId1)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId1));
        Cat cat2 = catRepository.findById(catId2)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId2));
        cat1.unfriend(cat2);
        cat2.unfriend(cat1);
        catRepository.saveAll(List.of(cat1, cat2));

    }

    @Override
    public CatDto getCatById(int id) {
       return catRepository.findById(id)
                .orElseThrow(() -> CatServiceException.noSuchCat(id)).asDto();
    }

    @Override
    public List<CatDto> findAllCats() {
        return catRepository.findAll()
                .stream()
                .map(CatMapper::asDto)
                .toList();
    }

    public List<CatDto> findAllOwnersCats() {
        Owner owner = getCurrentOwner();
        return catRepository.findAll()
                .stream()
                .filter(cat -> cat.getOwner().getId() == owner.getId())
                .map(CatMapper::asDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<CatDto> findAllFriends(int id) {
        return catRepository.findById(id)
                .orElseThrow(() -> CatServiceException.noSuchCat(id))
                .getFriends()
                .stream()
                .map(CatMapper::asDto)
                .toList();
    }

    @Override
    public void removeCat(int id) {
        catRepository.deleteById(id);
    }

    @Override
    public List<CatDto> findAllCatsByBreed(String breedName) {
        Breed breed = Breed.findByValue(breedName);
        return catRepository.findAll()
                .stream()
                .filter(cat -> cat.getBreed().equals(breed))
                .map(CatMapper::asDto)
                .toList();
    }

    @Override
    public List<CatDto> findOwnerCatsByBreed(String breedName) {
        Breed breed = Breed.findByValue(breedName);
        Owner owner = getCurrentOwner();
        return catRepository.findAll()
                .stream()
                .filter(cat -> cat.getBreed().equals(breed) && cat.getOwner().getId() == owner.getId())
                .map(CatMapper::asDto)
                .toList();
    }

    @Override
    public List<CatDto> findAllCatsByColour(String colourName) {
        Colour colour = Colour.findByValue(colourName);
        return catRepository.findAll()
                .stream()
                .filter(cat -> cat.getColour().equals(colour))
                .map(CatMapper::asDto)
                .toList();
    }

    @Override
    public List<CatDto> findOwnerCatsByColour(String colourName) {
        Colour colour = Colour.findByValue(colourName);
        Owner owner = getCurrentOwner();
        return catRepository.findAll()
                .stream()
                .filter(cat -> cat.getColour().equals(colour) && cat.getOwner().getId() == owner.getId())
                .map(CatMapper::asDto)
                .toList();
    }

    public boolean checkOwner(int id){
        Owner tmp = getCurrentOwner();
        if (!catRepository.existsById(id)) {
            throw CatServiceException.noSuchCat(id);
        }
        Cat cat = catRepository.getReferenceById(id);
        return cat.getOwner().getId() == tmp.getId();
    }

    private Owner getCurrentOwner(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findUserByName(username);
        if (user.isEmpty()){
            throw CatServiceException.noUser(username);
        }
        return user.get().getOwner();
    }
}
