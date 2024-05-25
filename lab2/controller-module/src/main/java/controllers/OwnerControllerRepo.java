package controllers;

import dto.CatDto; 
import dto.OwnerDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.OwnerServiceRepo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/owner/")
public class OwnerControllerRepo implements IOwnerController{

    private final OwnerServiceRepo serviceRepository;

    @Autowired
    public OwnerControllerRepo(OwnerServiceRepo serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    @PostMapping()
    public OwnerDto createOwner(@Valid @RequestBody OwnerDto ownerDto) {
        return serviceRepository.createOwner(ownerDto.name(), ownerDto.birthDate());
    }

    @PostMapping("create")
    public OwnerDto createOwner(@RequestParam(name = "name") String name,
                                @RequestParam(name = "date") LocalDate date) {
        return serviceRepository.createOwner(name, date);
    }

    @Override
    @GetMapping("{id}")
    public OwnerDto getOwnerById(@PathVariable("id") int id) {
        return serviceRepository.getOwnerById(id);
    }

    @Override
    @GetMapping("cats/{id}")
    public List<CatDto> findAllCats(@PathVariable("id") int id) {
        return serviceRepository.findAllCats(id);
    }

    @Override
    @GetMapping()
    public List<OwnerDto> findAllOwners() {
        return serviceRepository.findAllOwners();
    }

    @Override
    @DeleteMapping("{id}")
    public void removeOwner(@PathVariable("id")   int id) {
        serviceRepository.removeOwner(id);
    }
}
