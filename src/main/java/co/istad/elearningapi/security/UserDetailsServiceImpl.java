package co.istad.elearningapi.security;

import co.istad.elearningapi.api.user.User;
import co.istad.elearningapi.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameAndIsDeleted(
                username,
                false
        ).orElseThrow(() ->
                new UsernameNotFoundException("Username does not found!"));

        log.info("User details: {}", user);

        return new CustomUserDetails(user);
    }
}
