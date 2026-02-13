package com.petverse.exception;

public class BusinessException extends  RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
