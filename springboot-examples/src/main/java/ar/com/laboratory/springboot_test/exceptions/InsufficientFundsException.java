package ar.com.laboratory.springboot_test.exceptions;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
