package com.learn.UserApp.exception;

public class ResourceNotFoundException extends  RuntimeException{

    public ResourceNotFoundException(String s) {
        super(s);
    }

    public ResourceNotFoundException(){
        super("Resource not found on the server !");
    }
}