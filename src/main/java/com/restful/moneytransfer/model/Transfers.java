package com.restful.moneytransfer.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Nawaz
 *
 */
@XmlRootElement(name = "transfers")
public class Transfers {

    /**
     * List of transfers
     */
    private List<Transfer> transfers;

    /**
     * @return the transfers
     */
    public List<Transfer> getTransfers() {
        return transfers;
    }

    /**
     * @param transfers
     *            the transfers to set
     */
    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

}
