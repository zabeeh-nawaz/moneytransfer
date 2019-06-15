package com.restful.moneytransfer.controller;

import static com.restful.moneytransfer.App.LOGGER;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restful.moneytransfer.exception.MissingResourceException;
import com.restful.moneytransfer.model.Account;
import com.restful.moneytransfer.model.Accounts;
import com.restful.moneytransfer.model.Transfer;
import com.restful.moneytransfer.model.Transfers;
import com.restful.moneytransfer.service.MoneyTransferService;
import com.restful.moneytransfer.service.MoneyTransferServiceImpl;

/**
 * @author Nawaz
 *
 */
@Path("/")
public class MoneyTransferController {

    /**
     * Singleton instance of MoneyTransferService.
     */
    private MoneyTransferService service = MoneyTransferServiceImpl
            .getService();

    /**
     * @param account to be created
     * @return response entity with status and message
     * @throws Exception
     */
    @POST
    @Path("account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(final Account account) throws Exception {
        LOGGER.trace("MoneyTransferController.createAccount has been invoked");
        Long id = service.createAccount(account);
        String result = "created account with id " + id;
        return Response.status(HttpServletResponse.SC_CREATED).entity(result)
                .build();
    }

    /**
     * @param id
     *            of the account to be fetched
     * @return Account object of the given id
     * @throws MissingResourceException
     */
    @GET
    @Path("account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(final @PathParam("id") Long id)
            throws MissingResourceException {
        LOGGER.trace("MoneyTransferController.getAccount has been invoked");
        Account account = service.getAccount(id);
        return Response.status(HttpServletResponse.SC_OK).entity(account)
                .build();
    }

    /**
     * @return list of accounts in the database
     */
    @GET
    @Path("accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts() {
        LOGGER.trace("MoneyTransferController.getAccounts has been invoked");
        Accounts accountsList = new Accounts();
        accountsList.setAccounts(service.getAccounts());
        return Response.status(HttpServletResponse.SC_OK).entity(accountsList)
                .build();
    }

    /**
     * @param id
     *            of the account to be deleted
     * @return response entity with status and message
     * @throws MissingResourceException
     */
    @DELETE
    @Path("account/{id}")
    public Response deleteAccount(final @PathParam("id") Long id)
            throws MissingResourceException {
        LOGGER.trace("MoneyTransferController.deleteAccount has been invoked");
        service.deleteAccount(id);
        String result = "deleted account with id " + id;
        return Response.status(HttpServletResponse.SC_NO_CONTENT).entity(result)
                .build();
    }

    /**
     * @param request with details of transfer
     * @return response entity with status and message
     * @throws Throwable
     */
    @POST
    @Path("transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTransaction(final Transfer request) throws Throwable {
        LOGGER.trace(
                "MoneyTransferController.createTransaction has been invoked");
        Long id = service.createTransaction(request.getAmount(),
                request.getSenderAccountNumber(),
                request.getReceiverAccountNumber());
        String result = "created transaction with id " + id;
        return Response.status(HttpServletResponse.SC_CREATED).entity(result)
                .build();
    }

    /**
     * @param id
     *            of transaction to be fetched
     * @return transaction of the given id
     * @throws MissingResourceException
     */
    @GET
    @Path("transaction/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransaction(final @PathParam("id") Long id)
            throws MissingResourceException {
        LOGGER.trace("MoneyTransferController.getTransaction has been invoked");
        Transfer transfer = service.getTransfer(id);
        return Response.status(HttpServletResponse.SC_OK).entity(transfer)
                .build();
    }

    /**
     * @return transactions from database
     */
    @GET
    @Path("transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactions() {
        LOGGER.trace(
                "MoneyTransferController.getTransactions has been invoked");
        Transfers transfertsList = new Transfers();
        transfertsList.setTransfers(service.getTransfers());
        return Response.status(HttpServletResponse.SC_OK).entity(transfertsList)
                .build();
    }

    /**
     * @param id
     *            of the transaction to be deleted
     * @return response entity with status and message
     * @throws MissingResourceException
     */
    @DELETE
    @Path("transaction/{id}")
    public Response deleteTransaction(final @PathParam("id") Long id)
            throws MissingResourceException {
        LOGGER.trace(
                "MoneyTransferController.deleteTransaction has been invoked");
        service.deleteTransfer(id);
        String result = "deleted transaction with id " + id;
        return Response.status(HttpServletResponse.SC_NO_CONTENT).entity(result)
                .build();
    }

}
