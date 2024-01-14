package co.istad.elearningapi.api.auth;

import co.istad.elearningapi.base.BaseSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    BaseSuccess<?> login(@RequestBody LoginDto loginDto) {
        return BaseSuccess.builder()
                .data(authService.login(loginDto))
                .code(HttpStatus.OK.value())
                .message("User has been found successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
