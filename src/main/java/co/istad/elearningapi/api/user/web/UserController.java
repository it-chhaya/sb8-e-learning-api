package co.istad.elearningapi.api.user.web;

import co.istad.elearningapi.api.user.UserService;
import co.istad.elearningapi.base.BaseSuccess;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@Valid @RequestBody UserCreationDto creationDto) {
        userService.createNew(creationDto);
    }

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/enable")
    void enableById(@PathVariable Long id) {
        userService.enableById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/disable")
    void disableById(@PathVariable Long id) {
        userService.disableById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
