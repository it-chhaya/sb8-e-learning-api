package co.istad.elearningapi.api.user.web;

import co.istad.elearningapi.api.user.UserService;
import co.istad.elearningapi.base.BaseSuccess;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('CSTAD_user:update')")
    @PutMapping("/me/profile")
    BaseSuccess<?> editProfile(@RequestBody ProfileDto profileDto) {
        UserDto userDto = userService.editProfile(profileDto.profile());
        return BaseSuccess.builder()
                .data(userDto)
                .code(HttpStatus.OK.value())
                .message("User profile has been edited successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasAuthority('CSTAD_user:read')")
    @GetMapping("/me")
    BaseSuccess<?> findMe() {
        return BaseSuccess.builder()
                .data(userService.findMe())
                .code(HttpStatus.OK.value())
                .message("User profile has been found successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasAuthority('CSTAD_user:read')")
    @GetMapping
    BaseSuccess<?> findList() {
        return BaseSuccess.builder()
                .data(Map.of("data", "Get success"))
                .code(HttpStatus.OK.value())
                .message("User has been found successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasAuthority('CSTAD_user:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@Valid @RequestBody UserCreationDto creationDto) {
        userService.createNew(creationDto);
    }

    @PreAuthorize("hasAuthority('CSTAD_user:update')")
    @PutMapping("/{id}")
    BaseSuccess<?> editById(@PathVariable Long id, @Valid @RequestBody UserEditionDto editionDto) {
        return BaseSuccess.builder()
                .data(userService.editById(id, editionDto))
                .code(HttpStatus.OK.value())
                .message("User has been edited successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasAuthority('CSTAD_user:read')")
    @GetMapping("/{id}")
    BaseSuccess<?> findById(@PathVariable Long id) {
        return BaseSuccess.builder()
                .data(userService.findById(id))
                .code(HttpStatus.OK.value())
                .message("User has been found successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasAuthority('CSTAD_user:update')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/enable")
    void enableById(@PathVariable Long id) {
        userService.enableById(id);
    }

    @PreAuthorize("hasAuthority('CSTAD_user:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/disable")
    void disableById(@PathVariable Long id) {
        userService.disableById(id);
    }

    @PreAuthorize("hasAuthority('CSTAD_user:delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
