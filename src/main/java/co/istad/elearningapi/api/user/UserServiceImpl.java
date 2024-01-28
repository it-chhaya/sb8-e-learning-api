package co.istad.elearningapi.api.user;

import co.istad.elearningapi.api.user.web.UserCreationDto;
import co.istad.elearningapi.api.user.web.UserDto;
import co.istad.elearningapi.api.user.web.UserEditionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto editProfile(String profile) {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        User user = userRepository.findByUsernameAndIsDeletedAndIsVerified(jwt.getId(), false, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User has not been found"));

        user.setProfile(profile);
        userRepository.save(user);

        List<String> authorities = List.of(jwt.getClaimAsString("authorities").split(" "));

        return userMapper.toUserDto(user, authorities);
    }

    @Override
    public UserDto findMe() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        /*log.info("auth: {}", jwt.getClaimAsString("authorities"));
        log.info("auth: {}", jwt.getTokenValue());
        log.info("auth: {}", jwt.getId());*/
        User user = userRepository.findByUsernameAndIsDeletedAndIsVerified(jwt.getId(), false, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User has not been found"));

        List<String> authorities = List.of(jwt.getClaimAsString("authorities").split(" "));

        return userMapper.toUserDto(user, authorities);
    }

    @Override
    public void createNew(UserCreationDto creationDto) {

        // Check username
        if (userRepository.existsByUsername(creationDto.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username is already existed!");
        }

        // Check email
        // Check username
        if (userRepository.existsByEmail(creationDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email is already existed!");
        }

        List<Role> roles = (List<Role>)
                roleRepository.findAllById(creationDto.roleIds());
        Set<Role> userRoles = new HashSet<>(roles);

        User user = userMapper.fromUserCreationDto(creationDto);
        user.setIsDeleted(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        userRepository.save(user);
    }

    @Override
    public UserDto editById(Long id, UserEditionDto editionDto) {

        // Check user ID has or not
        User foundUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("User Id = %d has not found in database", id)));

        userMapper.fromUserEditionDto(foundUser, editionDto);
        userRepository.save(foundUser);

        return this.findById(foundUser.getId());
    }

    @Override
    public UserDto findById(Long id) {

        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User Id = %d has not found in database", id)
                ));

        List<String> authorities = new ArrayList<>();

        foundUser.getRoles().forEach(role -> {
            role.getAuthorities().forEach(authority -> {
                authorities.add(authority.getName());
            });
        });

        return userMapper.toUserDto(foundUser, authorities);
    }

    @Override
    public Page<?> findList(int pageNumber, int pageSize) {
        return null;
    }

    @Transactional
    @Override
    public void enableById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User Id = %d has not found in database", id)
            );
        }
        userRepository.enable(id);
    }

    @Transactional
    @Override
    public void disableById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User Id = %d has not found in database", id)
            );
        }
        userRepository.disable(id);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User Id = %d has not found in database", id)
            );
        }
        userRepository.deleteById(id);
    }
}
