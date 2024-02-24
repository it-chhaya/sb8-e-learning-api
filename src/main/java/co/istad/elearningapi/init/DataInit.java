package co.istad.elearningapi.init;

import co.istad.elearningapi.api.user.Role;
import co.istad.elearningapi.api.user.RoleRepository;
import co.istad.elearningapi.api.user.User;
import co.istad.elearningapi.api.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //@PostConstruct
    void init() {

        // Insert role: ADMIN, USER, STUDENT, INSTRUCTOR
        Role user = Role.builder().name("USER").build();
        Role admin = Role.builder().name("ADMIN").build();
        Role student = Role.builder().name("STUDENT").build();
        Role instructor = Role.builder().name("INSTRUCTOR").build();
        roleRepository.saveAll(List.of(user, admin, student, instructor));

        // Insert user:
        User userAdmin = User.builder()
                .username("admin")
                .email("admin@istad.co")
                .password(passwordEncoder.encode("admin@123"))
                .familyName("Chan")
                .givenName("Chhaya")
                .gender("Male")
                .dob(LocalDate.of(1990, 11, 11))
                .biography("Teacher of Spring framework")
                .isDeleted(false)
                .roles(Set.of(user, admin))
                .build();
        User userStudent = User.builder()
                .username("student")
                .email("student@istad.co")
                .password(passwordEncoder.encode("student@123"))
                .familyName("Jack")
                .givenName("Dawson")
                .gender("Male")
                .dob(LocalDate.of(1980, 11, 11))
                .biography("Lead action in Titanic")
                .isDeleted(false)
                .roles(Set.of(user, student))
                .build();
        User userInstructor = User.builder()
                .username("instructor")
                .email("instructor@istad.co")
                .password(passwordEncoder.encode("instructor@123"))
                .familyName("Kit")
                .givenName("Tara")
                .gender("Female")
                .dob(LocalDate.of(2000, 11, 11))
                .biography("IT Instructor at CSTAD")
                .isDeleted(false)
                .roles(Set.of(user, instructor))
                .build();
        userRepository.save(userAdmin);
        userRepository.save(userStudent);
        userRepository.save(userInstructor);
    }

}
