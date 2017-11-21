package com.nuno.payments.interfaces.common;

import com.nuno.payments.api.generated.dto.ErrorResponse;
import com.nuno.payments.api.generated.dto.FieldErrorResponse;
import com.nuno.payments.domain.model.exception.InvalidActionException;
import com.nuno.payments.domain.model.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleError(MethodArgumentNotValidException exception) {
        log.info("Method argument not valid");
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<FieldErrorResponse> errorList = fieldErrors.stream()
                .map((FieldError fe) -> {
                    FieldErrorResponse fr = new FieldErrorResponse();
                    fr.setMessage(fe.getCode());
                    fr.setFieldName(fe.getField());
                    return fr;
                })
                .collect(Collectors.toList());
        ErrorResponse er = new ErrorResponse();
        er.setErrors(errorList);
        return er;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleError(ResourceNotFoundException exception) {
        log.info("Resource not found, with message: {}", exception.getMessage());
        List<FieldErrorResponse> errorList = Stream.of(exception.getMessage())
                .map(e -> {
                    FieldErrorResponse fr = new FieldErrorResponse();
                    fr.setMessage(e);
                    fr.setFieldName("ID");
                    return fr;
                })
                .collect(Collectors.toList());
        ErrorResponse er = new ErrorResponse();
        er.setErrors(errorList);
        return er;
    }

    @ExceptionHandler(InvalidActionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleError(InvalidActionException exception) {
        log.info("Invalid action, with message: {}", exception.getMessage());
        List<FieldErrorResponse> errorList = Stream.of(exception.getMessage())
                .map(e -> {
                    FieldErrorResponse fr = new FieldErrorResponse();
                    fr.setMessage(e);
                    fr.setFieldName("ID");
                    return fr;
                })
                .collect(Collectors.toList());
        ErrorResponse er = new ErrorResponse();
        er.setErrors(errorList);
        return er;
    }


}
