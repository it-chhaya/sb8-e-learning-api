package co.istad.elearningapi.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("""
        DELETE User AS u
        WHERE u.username = ?1
    """)
    void deletePermanently(String username);

    @Modifying
    @Query("""
        UPDATE User AS u SET u.isDeleted = TRUE
        WHERE u.id = ?1
    """)
    void disable(Long id);

    @Modifying
    @Query("""
        UPDATE User AS u SET u.isDeleted = FALSE
        WHERE u.id = ?1
    """)
    void enable(Long id);

    @Transactional
    @Modifying
    @Query("""
        UPDATE User AS u SET u.biography = ?2
        WHERE u.username = ?1
    """)
    void updateBiography(String username, String newBiography);

    @Query("""
        SELECT u
        FROM User AS u
        WHERE (u.username = ?1 OR u.email = ?1)
        AND
        u.isDeleted = ?2
    """)
    Optional<User> login(String usernameOrEmail, Boolean isDeleted);

    Optional<User> findByUsernameAndIsDeleted(String username, Boolean isDeleted);

    List<User> findByFamilyNameContainsIgnoreCase(String familyName);

}
