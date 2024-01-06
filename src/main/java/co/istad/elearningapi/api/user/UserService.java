package co.istad.elearningapi.api.user;

import co.istad.elearningapi.api.user.web.UserCreationDto;
import co.istad.elearningapi.api.user.web.UserDto;
import co.istad.elearningapi.api.user.web.UserEditionDto;
import org.springframework.data.domain.Page;

public interface UserService {

    // Create new user:
    // Return type is DTO:
    // Request data is DTO:
    void createNew(UserCreationDto creationDto);

    UserDto editById(Long id, UserEditionDto editionDto);

    UserDto findById(Long id);

    Page<?> findList(int pageNumber, int pageSize);

    void enableById(Long id);

    void disableById(Long id);

    void deleteById(Long id);

}
