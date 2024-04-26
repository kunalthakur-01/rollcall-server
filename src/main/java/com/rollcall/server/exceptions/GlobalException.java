package com.rollcall.server.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rollcall.server.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException( ResourceNotFoundException ex) {
        ApiResponse apiResponse = new ApiResponse( ex.getMessage(), false);
        // return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        // *****************************************************or****************************************************************
        return ResponseEntity.status(404).body(apiResponse);
    }
    
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiResponse> internalServerException( InternalServerException ex) {
        ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false);
        // return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        // *****************************************************or****************************************************************
        return ResponseEntity.status(500).body(apiResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponse> internalServerException( ResourceAlreadyExistException ex) {
        ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false);
        // return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
        // *****************************************************or****************************************************************
        return ResponseEntity.status(409).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException( MethodArgumentNotValidException ex) {
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField(); 
            String message = error.getDefaultMessage();

            resp.put(fieldName, message);
        });
        // return new ResponseEntity<>(resp, HttpStatus.valueOf(500));
        // *****************************************************or***************************************************************
        return ResponseEntity.status(500).body(resp);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleCustomException( CustomException ex) {
        ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false);
        // return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(ex.getCode()));
        // *****************************************************or****************************************************************
        return ResponseEntity.status(ex.getCode()).body(apiResponse);
    }

    @ExceptionHandler(MultipleException.class)
    public ResponseEntity<List<String>> handleMultipleExceptions( MultipleException ex) {
        List<String> errs = ex.getErrros();
        // return new ResponseEntity<>(resp, HttpStatus.valueOf(500));
        // *****************************************************or***************************************************************
        return ResponseEntity.status(500).body(errs);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> exceptionHandler() {
        ApiResponse apiResponse = new ApiResponse("Credentials Invalid !!", false);
        return ResponseEntity.status(400).body(apiResponse);
    }
}
