package controllers;

import dto.CatDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICatController {
    CatDto createCat(CatDto catDto);
    ResponseEntity<CatDto> getCatById(int id);
    ResponseEntity<List<CatDto>> findAllFriends(int id);
    List<CatDto> findAllCats();
    HttpStatus removeCat(int id);
    List<CatDto> findCatByBreed(String breed);
    List<CatDto> findCatByColour(String colour);
    void makeFriends(int catId1, int catId2);
    void unfriendCats(int catId1, int catId2);
}
