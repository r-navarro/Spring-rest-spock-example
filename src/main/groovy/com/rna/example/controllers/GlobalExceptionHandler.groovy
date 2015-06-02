package com.rna.example.controllers

import com.rna.example.exceptions.TechnicalException
import com.rna.example.exceptions.pojo.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(TechnicalException)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    def ErrorMessage exceptionHandler(TechnicalException e, HttpServletRequest request) {
        return new ErrorMessage(message: e.getMessage(), request: request.getPathInfo() ?: request.getServletPath())
    }
}
