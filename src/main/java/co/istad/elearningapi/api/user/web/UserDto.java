package co.istad.elearningapi.api.user.web;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record UserDto(Long id,
                      String familyName,
                      String givenName,
                      String profile,
                      String gender,
                      LocalDate dob,
                      String biography,
                      List<String> authorities) {
}
