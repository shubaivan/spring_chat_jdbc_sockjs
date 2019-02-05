package com.spdu.web.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spdu.bll.custom_exceptions.UserException;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    private void setExceptionDetails(HttpServletRequest req, Exception e) {
        logger.info(req.getRequestURL());
        logger.error(e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "IOException")
    @ExceptionHandler(IOException.class)
    public void handleIOException(HttpServletRequest req, IOException e) {
        setExceptionDetails(req, e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "JsonProcessingException")
    @ExceptionHandler(JsonProcessingException.class)
    public void handleJsonProcessingException(HttpServletRequest req, JsonProcessingException e) {
        setExceptionDetails(req, e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UserException")
    @ExceptionHandler(UserException.class)
    public void handleUserException(HttpServletRequest req, UserException e) {
        setExceptionDetails(req, e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "SQLException")
    @ExceptionHandler(SQLException.class)
    public void handleSQLException(HttpServletRequest req, SQLException e) {
        setExceptionDetails(req, e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UsernameNotFoundException")
    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException(HttpServletRequest req, UsernameNotFoundException e) {
        setExceptionDetails(req, e);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "DataNotFoundException")
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void handleEmptyResultDataAccessException(HttpServletRequest req, EmptyResultDataAccessException e) {
        setExceptionDetails(req, e);
    }
}
