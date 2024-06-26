package com.ujjwalgarg.jobportal.exception;

/**
 * EntityAlreadyExistsException
 */
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }

    public EntityAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
