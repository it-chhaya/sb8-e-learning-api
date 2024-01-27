package co.istad.elearningapi.api.auth;

import co.istad.elearningapi.base.BaseSuccess;
import jakarta.validation.Valid;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verify-user")
    BaseSuccess<?> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        authService.verifyUser(verifyUserDto.email(), verifyUserDto.verifiedCode());
        return BaseSuccess.builder()
                .data("http://localhost:3000/")
                .code(HttpStatus.OK.value())
                .message("User has been verified successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/verify-mail")
    BaseSuccess<?> verifyMail(@Valid @RequestBody VerifyMailDto verifyMailDto) {
        authService.verifyBySendMail(verifyMailDto.email());
        return BaseSuccess.builder()
                .data(verifyMailDto.email())
                .code(HttpStatus.OK.value())
                .message("Please check email to verify")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/refresh-token")
    BaseSuccess<?> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return BaseSuccess.builder()
                .data(authService.refreshToken(refreshTokenDto))
                .code(HttpStatus.OK.value())
                .message("Access token has been refreshed successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/login")
    BaseSuccess<?> login(@Valid @RequestBody LoginDto loginDto) {
        return BaseSuccess.builder()
                .data(authService.login(loginDto))
                .code(HttpStatus.OK.value())
                .message("User has been found successfully")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/register")
    BaseSuccess<?> register(@Valid @RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("You have been registered successfully, please check email confirmation")
                .status(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
