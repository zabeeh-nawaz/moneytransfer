package com.restful.moneytransfer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nawaz
 *
 */
@Entity
@Table(name = "TRANSACTION")
public class Transaction {

    /**
     * Primary key of the Transaction
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    /**
     * Account number of the sender
     */
    @Column(name = "SENDER_ACCOUNT_NUMBER")
    private String senderAccountNumber;

    /**
     * Account number of the receiver
     */
    @Column(name = "RECEIVER_ACCOUNT_NUMBER")
    private String receiverAccountNumber;

    /**
     * Amount transfered
     */
    @Column(name = "TRANSFER_AMOUNT")
    private Double transferAmount;

    /**
     * Default Constructor
     */
    public Transaction() {

    }

    /**
     * @param senderAccountNumber
     * @param receiverAccountNumber
     * @param transferAmount
     */
    public Transaction(String senderAccountNumber, String receiverAccountNumber,
            Double transferAmount) {
        setSenderAccountNumber(senderAccountNumber);
        setReceiverAccountNumber(receiverAccountNumber);
        setTransferAmount(transferAmount);
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

    /**
     * @return the transferAmount
     */
    public Double getTransferAmount() {
        return transferAmount;
    }

    /**
     * @param transferAmount
     *            the transferAmount to set
     */
    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

}
