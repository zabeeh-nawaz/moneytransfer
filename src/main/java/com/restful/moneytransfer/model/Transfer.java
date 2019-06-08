package com.restful.moneytransfer.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nawaz
 *
 */
@XmlRootElement()
public class Transfer {

    /**
     * Identifier of the transfer
     */
    private Long id;

    /**
     * Amount to transfer
     */
    private Double amount;

    /**
     * Account Number of the sender
     */
    private String senderAccountNumber;

    /**
     * Account Number of the receiver
     */
    private String receiverAccountNumber;

    /**
     * Default constructor
     */
    public Transfer() {
    }

    /**
     * @param id
     * @param amount
     * @param senderAccountNumber
     * @param receiverAccountNumber
     */
    public Transfer(Long id, Double amount, String senderAccountNumber,
            String receiverAccountNumber) {
        this.id = id;
        this.amount = amount;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the senderAccountNumber
     */
    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    /**
     * @param senderAccountNumber
     *            the senderAccountNumber to set
     */
    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    /**
     * @return the receiverAccountNumber
     */
    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    /**
     * @param receiverAccountNumber
     *            the receiverAccountNumber to set
     */
    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

}
