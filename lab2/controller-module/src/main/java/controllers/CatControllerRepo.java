package controllers;

import dto.CatDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import services.CatServiceRepo;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/cat/")
public class CatControllerRepo implements ICatController {

    private final CatServiceRepo serviceRepository;

    @Autowired
    public CatControllerRepo(CatServiceRepo serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    @PostMapping()
    public CatDto createCat(@Valid @RequestBody CatDto catDto) {
        return serviceRepository.createCat(
                catDto.name(),
                catDto.birthDate(),
                catDto.breed(),
                catDto.colour(),
                catDto.ownerId());
    }

    @PostMapping("create")
    public CatDto createCat(@RequestParam(name = "name") String name,
                            @RequestParam(name = "date") LocalDate date,
                            @RequestParam(name = "breed") String breedName,
                            @RequestParam(name = "colour") String colour,
                            @RequestParam(name = "ownerId") int ownerId) {
        return serviceRepository.createCat(name, date, breedName, colour, ownerId);
    }


    @Override
    @GetMapping("{id}")
    public ResponseEntity<CatDto> getCatById(@PathVariable("id") int id) {
        if (checkAdminAuth() || checkUserAuth(id)) {
            return new ResponseEntity<>(serviceRepository.getCatById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Override
    @GetMapping("friends/{id}")
    public ResponseEntity<List<CatDto>> findAllFriends(@PathVariable("id") int id) {
        if (checkAdminAuth() || checkUserAuth(id)) {
            return new ResponseEntity<>(serviceRepository.findAllFriends(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Override
    @GetMapping("getAll")
    public List<CatDto> findAllCats() {
        if (checkAdminAuth()) {
            return serviceRepository.findAllCats();
        }
        return serviceRepository.findAllOwnersCats();
    }

    @Override
    @DeleteMapping("{id}")
    public HttpStatus removeCat(@PathVariable("id") int id) {
        if (checkAdminAuth() || checkUserAuth(id)) {
            serviceRepository.removeCat(id);
            return HttpStatus.OK;
        }
        return HttpStatus.FORBIDDEN;
    }

    @Override
    @GetMapping("getBreed")
    public List<CatDto> findCatByBreed(@RequestParam(name = "breed") String breed) {
        if (checkAdminAuth()) {
            return serviceRepository.findAllCatsByBreed(breed);
        }
        return serviceRepository.findOwnerCatsByBreed(breed);
    }

    @Override
    @GetMapping("getColour")
    public List<CatDto> findCatByColour(@RequestParam(name = "colour") String colour) {
        if (checkAdminAuth()) {
            return serviceRepository.findAllCatsByColour(colour);
        }
        return serviceRepository.findOwnerCatsByColour(colour);
    }

    @Override
    @PutMapping("makeFriends")
    public void makeFriends(@RequestParam(name = "cat1") int catId1,
                            @RequestParam(name = "cat2") int catId2) {
        serviceRepository.makeFriends(catId1, catId2);
    }

    @Override
    @PutMapping("unfriend")
    public void unfriendCats(@RequestParam(name = "cat1") int catId1,
                             @RequestParam(name = "cat2") int catId2) {
        serviceRepository.unfriendCats(catId1, catId2);
    }

    private boolean checkAdminAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    private boolean checkUserAuth(int id) {
        return serviceRepository.checkOwner(id);
    }
}
