package com.restful.moneytransfer.exception;

/**
 * @author Nawaz
 *
 */
public class MissingResourceException extends Exception {
    /**
     * Serial version unique identifier
     */
    private static final long serialVersionUID = 8283758218874745471L;

    /**
     * Constructor to create MissingAccountException instance
     * @param message
     */
    public MissingResourceException(String message) {
        super(message);
    }

}
