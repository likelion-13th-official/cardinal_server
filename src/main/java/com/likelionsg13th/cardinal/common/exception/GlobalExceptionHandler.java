package com.likelionsg13th.cardinal.common.exception;

import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import com.likelionsg13th.cardinal.common.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ApiResponse> handleInvalidParameterException(InvalidParameterException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    private ResponseEntity<ApiResponse> buildErrorResponse(HttpStatus status, String message) {
        ApiResponse response = new ApiResponse(false, status.value(), message);
        return new ResponseEntity<>(response, status);
    }



    ///커스텀예외 (비즈니스)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode= ex.getErrodCode();

        ApiResponse response = new ApiResponse(false,errorCode.getHttpStatus().value(), errorCode.getMessage());

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


    // 쿼리 파라미터 없음
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        String message = ex.getParameterName() + " parameter is missing.";
        ApiResponse response = new ApiResponse(false,HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
