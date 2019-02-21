package com.spdu.bll.models.validator;

import com.google.common.base.Preconditions;
import com.spdu.bll.models.ResetPasswordDto;
import com.spdu.bll.models.UserRegisterDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class DtoValidator {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    private final String PASSWORD_REGEX = "^.*(?=.{4,10})(?=.*\\d)(?=.*[a-zA-Z]).*$";

    public void validateUserRegisterDto(UserRegisterDto userRegisterDto) throws IllegalArgumentException {
        Preconditions.checkArgument(isEmailValid(userRegisterDto.getEmail()), "Invalid email");
        Preconditions.checkArgument(isDateOfBirthCorrect(userRegisterDto.getDateOfBirth()), "Invalid date of birth");
        Preconditions.checkArgument(isPasswordValid(userRegisterDto.getPassword()), "Invalid password");
    }

    public void validateResetPasswordDto(ResetPasswordDto resetPasswordDto) throws IllegalArgumentException {
        Preconditions.checkArgument(isPasswordValid(resetPasswordDto.getPassword()), "Invalid password");
    }

    private boolean checkByRegexPattern(String data, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(data).matches();
    }

    private boolean isEmailValid(String email) {
        return checkByRegexPattern(email, EMAIL_REGEX);
    }

    private boolean isPasswordValid(String password) {
        return checkByRegexPattern(password, PASSWORD_REGEX);
    }

    private boolean checkDateBoundaries(LocalDate date, LocalDate min, LocalDate max) {
        return !date.isBefore(min) && !date.isAfter(max);
    }

    private boolean isDateOfBirthCorrect(LocalDate date) {
        return date != null
                && checkDateBoundaries(date, LocalDate.of(1919, 1, 1),
                LocalDate.now());
    }
}