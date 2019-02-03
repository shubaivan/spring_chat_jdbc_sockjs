package com.spdu.bll.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "IOException")
    @ExceptionHandler(IOException.class)
    public void handleIOException() {
        logger.error("IOException executed");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UserException")
    @ExceptionHandler(UserException.class)
    public void handleUserException() {
        logger.error("UserException executed");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "SQLException")
    @ExceptionHandler(SQLException.class)
    public void handleSQLException() {
        logger.error("SQLException executed");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UsernameNotFoundException")
    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException() {
        logger.error("UsernameNotFoundException executed");
    }
}
