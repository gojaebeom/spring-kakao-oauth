package kr.todoit.exception;

public interface ExceptionType {
    int getErrorCode();
    int getHttpStatus();
    String getErrorMessage();
}
