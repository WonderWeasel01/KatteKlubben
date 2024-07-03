package com.example.demo.Exceptions;

public class UserAlreadyExistException extends Exception
{
    public UserAlreadyExistException(String message){
        super(message);
    }

}
