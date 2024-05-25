package services;

import dto.UserDto;
import entity.Owner;
import entity.User;
import exceptions.UserServiceException;
import lombok.experimental.ExtensionMethod;
import mappers.CatMapper;
import mappers.UserMapper;
import models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.IOwnerRepo;
import repositories.IUserRepo;

@ExtensionMethod(UserMapper.class)
@Service
public class UserSecurityService implements IUserSecurityService {
    private final IUserRepo userRepository;
    private final IOwnerRepo ownerRepository;

    @Autowired
    public UserSecurityService(IUserRepo userRepository, IOwnerRepo ownerRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDto createUser(String name, String password, String roleName, int ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> UserServiceException.noOwner(ownerId));

        userRepository.findUserByName(name)
                .ifPresent(u -> {
                    throw UserServiceException.usernameAlreadyExist(name);
                });

        userRepository.findUserByOwner(owner)
                .ifPresent(u -> {
                    throw UserServiceException.ownerRegistered(ownerId);
                });

        Role role = Role.findByValue(roleName).orElseThrow(() -> UserServiceException.noRole(roleName) );

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userRepository.save(new User(name, encoder.encode(password), role, owner)).asDto();
    }
}
