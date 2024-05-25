package services;


import dto.CatDto;

import java.time.LocalDate;
import java.util.List;


public interface ICatService {
    CatDto createCat(String name, LocalDate birthDate, String breedName, String colourName, int ownerId);
    void makeFriends(int catId1, int catId2);
    void unfriendCats(int catId1, int catId2);
    CatDto getCatById(int id);
    List<CatDto> findAllCats();
    List<CatDto> findAllOwnersCats();
    List<CatDto> findAllFriends(int id);
    void removeCat(int id);
    List<CatDto> findAllCatsByBreed(String breedName);
    List<CatDto> findOwnerCatsByBreed(String breedName);

    List<CatDto> findOwnerCatsByColour(String colourName);
    List<CatDto> findAllCatsByColour(String colourName);
}
