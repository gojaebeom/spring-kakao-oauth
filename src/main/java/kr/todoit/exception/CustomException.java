package kr.todoit.exception;

import lombok.Getter;

public class CustomException extends RuntimeException{

    @Getter
    private ExceptionType exceptionType;

    public CustomException(ExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public String getErrorMessage(){
        return exceptionType.getErrorMessage();
    }

    public int getErrorCode(){
        return exceptionType.getErrorCode();
    }

    public int getHttpStatus(){
        return exceptionType.getHttpStatus();
    }
}
