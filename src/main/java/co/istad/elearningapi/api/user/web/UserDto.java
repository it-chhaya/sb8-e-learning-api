package co.istad.elearningapi.api.user.web;

import java.time.LocalDate;

public record UserDto(Long id,
                      String familyName,
                      String givenName,
                      String gender,
                      LocalDate dob,
                      String biography) {
}
