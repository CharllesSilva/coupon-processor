package com.charlles.coupon_processor.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("validation error");

        return ResponseEntity.badRequest()
                .body(new ErrorResponseDTO(400, message));
    }

    @ExceptionHandler(InvalidCouponException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCoupon(InvalidCouponException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDTO(400, ex.getMessage()));
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCouponNotFound(CouponNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(new ErrorResponseDTO(404, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return ResponseEntity.status(500)
                .body(new ErrorResponseDTO(500, "internal server error"));
    }

    @ExceptionHandler(CouponAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponseDTO> handleCouponAlreadyDeleted(CouponAlreadyDeletedException ex) {
        return ResponseEntity.status(409)
                .body(new ErrorResponseDTO(409, ex.getMessage()));
    }


}
