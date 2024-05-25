package services;

import dao.CatDao;
import dao.OwnerDao;
import dto.CatDto;
import exceptions.CatServiceException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import mappers.CatMapper;
import models.Breed;
import entity.Cat;
import models.Colour;
import entity.Owner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@ExtensionMethod(CatMapper.class)
@RequiredArgsConstructor
public class CatService {
    private final CatDao catDao;
    private final OwnerDao ownerDao;

    public CatDto createCat(String name, LocalDate birthDate, String breedName, String colourName, int ownerId) {
        Owner tmpOwner = ownerDao.getById(ownerId).orElseThrow(() -> CatServiceException.noSuchOwner(ownerId));

        Breed breed = Breed.findByValue(breedName);
        Colour colour = Colour.findByValue(colourName);

        Cat cat = new Cat(name, birthDate, breed, colour, tmpOwner, new HashSet<>());
        tmpOwner.addCat(cat);
        return catDao.update(cat).asDto();
    }

    public void makeFriends(int catId1, int catId2) {
        Cat cat1 = catDao.getById(catId1)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId1));
        Cat cat2 = catDao.getById(catId2)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId2));
        cat1.addFriend(cat2);
        cat2.addFriend(cat1);
        catDao.update(cat1);
    }

    public void unfriendCats(int catId1, int catId2) {
        Cat cat1 = catDao.getById(catId1)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId1));
        Cat cat2 = catDao.getById(catId2)
                .orElseThrow(() -> CatServiceException.noSuchCat(catId2));
        cat1.unfriend(cat2);
        cat2.unfriend(cat1);
        catDao.update(cat1);
        catDao.update(cat2);
    }

    public CatDto getCatById(int id) {
        return catDao.getById(id)
                .orElseThrow(() -> CatServiceException.noSuchCat(id))
                .asDto();
    }

    public List<CatDto> findAllCats() {
        return catDao.getAll().stream()
                .map(CatMapper::asDto)
                .toList();
    }

    public List<CatDto> findAllFriends(int id) {
        return catDao.getAllFriends(id).stream()
                .map(CatMapper::asDto)
                .toList();
    }

    public void removeCat(int id) {
        Cat toDelete = catDao.getById(id)
                .orElseThrow(() -> CatServiceException.noSuchCat(id));
        for (Cat friend : toDelete.getFriends()) {
            friend.unfriend(toDelete);
            catDao.update(friend);
        }
        toDelete.getFriends().clear();
        catDao.update(toDelete);
        catDao.remove(toDelete);
    }

    public List<CatDto> findCatsByBreed(String breedName) {
        Breed breed = Breed.findByValue(breedName);
        return catDao.getByBreed(breed).stream().map(CatMapper::asDto).toList();
    }

    public List<CatDto> findCatsByColour(String colourName) {
        Colour colour = Colour.findByValue(colourName);
        return catDao.getByColour(colour).stream().map(CatMapper::asDto).toList();
    }
}
