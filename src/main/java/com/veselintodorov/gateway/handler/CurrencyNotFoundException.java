package com.veselintodorov.gateway.handler;

public class CurrencyNotFoundException extends Exception {
    public CurrencyNotFoundException(String message) {
        super("Currency " + message + " was not found");
    }
}
