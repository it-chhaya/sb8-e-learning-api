package co.istad.elearningapi.api.user;

import co.istad.elearningapi.api.auth.RegisterDto;
import co.istad.elearningapi.api.user.web.UserCreationDto;
import co.istad.elearningapi.api.user.web.UserDto;
import co.istad.elearningapi.api.user.web.UserEditionDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromUserCreationDto(UserCreationDto creationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUserEditionDto(@MappingTarget User user,
                            UserEditionDto userEditionDto);

    @Mapping(source = "authorities", target = "authorities")
    UserDto toUserDto(User user, List<String> authorities);

    UserCreationDto mapRegisterDtoToUserCreationDto(RegisterDto registerDto);

}
