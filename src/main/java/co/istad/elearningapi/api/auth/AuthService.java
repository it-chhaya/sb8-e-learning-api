package co.istad.elearningapi.api.auth;

public interface AuthService {

    AuthDto refreshToken(RefreshTokenDto refreshTokenDto);

    AuthDto login(LoginDto loginDto);

}
