package org.fourchan.java4chan.exception;

public class ThreadNotFoundException extends RuntimeException{
    public ThreadNotFoundException() {
    }

    public ThreadNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
