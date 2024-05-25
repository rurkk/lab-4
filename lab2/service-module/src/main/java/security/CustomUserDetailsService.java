package security;

import entity.User;
import exceptions.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repositories.IUserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepo userRepository;

    @Autowired
    public CustomUserDetailsService(IUserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User tmpUser = userRepository.findUserByName(username).orElseThrow(() -> UserServiceException.noUser(username));
        return new AppUserDetails(tmpUser);
    }
}
