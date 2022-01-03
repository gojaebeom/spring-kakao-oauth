package kr.todoit.controller;

import kr.todoit.exception.CustomException;
import kr.todoit.exception.ValidExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionController extends RuntimeException {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException e) {
        log.error("---Error Handler---");
        log.error(e.getExceptionType() instanceof ValidExceptionType ? "VALID_FALSE" : e.getExceptionType().toString());
        log.error(e.getErrorMessage());
        log.error(String.valueOf(e.getErrorCode()));
        log.error(String.valueOf(e.getHttpStatus()));

        Map<String, Object> error = new HashMap<>();
        String message = e.getErrorMessage() != null ? e.getErrorMessage() : "요청을 처리하지 못하였습니다.";
        error.put("type", e.getExceptionType() instanceof ValidExceptionType ? "VALID_FALSE" : e.getExceptionType());
        error.put("message", message);
        error.put("errorCode", e.getErrorCode());
        error.put("statusCode", e.getHttpStatus());

        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        return ResponseEntity.ok().body(response);
    }
}
