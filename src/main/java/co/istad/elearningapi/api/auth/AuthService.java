package co.istad.elearningapi.api.auth;

import java.util.Map;

public interface AuthService {

    AuthDto refreshToken(RefreshTokenDto refreshTokenDto);

    AuthDto login(LoginDto loginDto);

    void register(RegisterDto registerDto);

    // verify
    void verifyBySendMail(String email);
    void verifyUser(String email, String verifiedCode);

    // reset password
}
