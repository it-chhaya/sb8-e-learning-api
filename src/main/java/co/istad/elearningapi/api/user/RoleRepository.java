package co.istad.elearningapi.api.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query(value = "SELECT r FROM Role AS r WHERE r.name = 'USER'")
    Role USER();

    @Query(value = "SELECT r FROM Role AS r WHERE r.name = 'ADMIN'")
    Role ADMIN();

    @Query(value = "SELECT r FROM Role AS r WHERE r.name = 'STUDENT'")
    Role STUDENT();

    @Query(value = "SELECT r FROM Role AS r WHERE r.name = 'INSTRUCTOR'")
    Role INSTRUCTOR();

}
