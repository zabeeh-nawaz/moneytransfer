package restful.moneytransfer.controller;

import static com.restful.moneytransfer.App.LOGGER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.restful.moneytransfer.controller.MoneyTransferController;
import com.restful.moneytransfer.entity.BankAccount;
import com.restful.moneytransfer.entity.Transaction;
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
public class MoneyTransferControllerTest {

    /**
     * Controller to test
     */
    MoneyTransferController controller = new MoneyTransferController();

    /**
     * Sets initial Data
     */
    @Before
    public void setData() {
        LOGGER.trace("MoneyTransferControllerTest.setData() initializing test data");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MoneyTransferPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        BankAccount accountOne = new BankAccount("1012345678", "Tom", new Double(20000.00));
        BankAccount accountTwo = new BankAccount("2012345678", "Angella", new Double(5000.00));
        em.persist(accountOne);
        em.persist(accountTwo);

        Transaction transaction = new Transaction("1012345678", "2012345678", 5000.00);
        em.persist(transaction);

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Unit Test to test create Account
     */
    @Test
    public void testCreateAccount() {
        LOGGER.trace("MoneyTransferControllerTest.testCreateAccount is initiated");
        // Arrange
        Account account = new Account(null, "Bob", "123", 20000.00);

        // Act
        Response response = controller.createAccount(account);

        // Assert
        String entity = (String) response.getEntity();
        assertNotNull(response);
        assertTrue(entity.contains("created account with id"));
    }

    /**
     * Unit Test to test get Account
     * @throws MissingResourceException
     */
    @Test
    public void testGetAccount() throws MissingResourceException {
        LOGGER.trace("MoneyTransferControllerTest.testGetAccount is initiated");
        // Act
        Response response = controller.getAccount(1l);

        // Assert
        Account account = (Account) response.getEntity();
        assertNotNull(response);
        assertNotNull(account);
    }

    /**
     * Unit Test to test get Accounts
     */
    @Test
    public void testGetAccounts() {
        LOGGER.trace("MoneyTransferControllerTest.testGetAccounts is initiated");
        // Act
        Response response = controller.getAccounts();

        // Assert
        Accounts accounts = (Accounts) response.getEntity();
        assertNotNull(response);
        assertTrue(accounts.getAccounts().size() >= 2);
    }

    /**
     * Unit test to test delete Account
     * @throws MissingResourceException
     */
    @Test
    public void testDeleteAccount() throws MissingResourceException {
        LOGGER.trace("MoneyTransferControllerTest.testDeleteAccount is initiated");
        //Arrange
        MoneyTransferService service = MoneyTransferServiceImpl.getService();
        Long id = service.createAccount(new Account(null, "Hank", "7012345678", 4000.00));

        //Act
        Response response = controller.deleteAccount(id);

        //Assert
        assertEquals(204, response.getStatus());
    }

    /**
     * Unit test to test create transfer
     * @throws Throwable
     */
    @Test
    public void testCreateTransfer() throws Throwable {
        LOGGER.trace("MoneyTransferControllerTest.testCreateTransfer is initiated");
        // Arrange
        Transfer request = new Transfer(null, 20000.00, "1012345678", "2012345678");

        // Act
        Response response = controller.createTransaction(request);

        // Assert
        String entity = (String) response.getEntity();
        assertNotNull(response);
        assertTrue(entity.contains("created transaction with id"));
    }

    /**
     * Unit test to test get transaction
     * @throws MissingResourceException
     */
    @Test
    public void testGetTransaction() throws MissingResourceException {
        LOGGER.trace("MoneyTransferControllerTest.testGetTransaction is initiated");
        // Act
        Response response = controller.getTransaction(1l);

        // Assert
        Transfer transfer = (Transfer) response.getEntity();
        assertNotNull(response);
        assertNotNull(transfer);
    }

    /**
     * Unit test to test get transactions
     */
    @Test
    public void testGetTransactions() {
        LOGGER.trace("MoneyTransferControllerTest.testGetTransactions is initiated");
        // Act
        Response response = controller.getTransactions();

        // Assert
        Transfers transfers = (Transfers) response.getEntity();
        assertNotNull(response);
        assertTrue(transfers.getTransfers().size() >= 1);
    }

    /**
     * @throws Throwable
     */
    @Test
    public void testDeleteTransfer() throws Throwable {
        LOGGER.trace("MoneyTransferControllerTest.testDeleteTransfer is initiated");
        // Arrange
        MoneyTransferService service = MoneyTransferServiceImpl.getService();
        Long id = service.createTransaction(20000.00, "1012345678", "2012345678");

        // Act
        Response response = controller.deleteTransaction(id);

        // Assert
        assertEquals(204, response.getStatus());
    }

}
