package com.restful.moneytransfer.exception;

/**
 * @author Nawaz
 *
 */
public class InsufficientFundsException extends Exception {

    /**
     * Serial version unique identifier
     */
    private static final long serialVersionUID = 7538076539731557380L;

    /**
     * Default Constructor
     */
    public InsufficientFundsException() {
        super("Insufficient Funds in sender account to complete "
                + "the transfer.");
    }

}
