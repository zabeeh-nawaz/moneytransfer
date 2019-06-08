package restful.moneytransfer.exception;

import static com.restful.moneytransfer.App.LOGGER;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.restful.moneytransfer.controller.MoneyTransferController;
import com.restful.moneytransfer.entity.BankAccount;
import com.restful.moneytransfer.exception.InsufficientFundsException;
import com.restful.moneytransfer.model.Transfer;

/**
 * @author Nawaz
 *
 */
public class ExceptionHandlerTest {

    /**
     *  Instance of controller
     */
    MoneyTransferController controller = new MoneyTransferController();

    /**
     *  Sets up initial data
     */
    @Before
    public void setData() {
        LOGGER.trace("ExceptionHandlerTest.setData() initializing test data");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MoneyTransferPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        BankAccount accountOne = new BankAccount("1012345678", "Tom", new Double(20000.00));
        BankAccount accountTwo = new BankAccount("2012345678", "Angella", new Double(5000.00));
        em.persist(accountOne);
        em.persist(accountTwo);

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Unit test to test throw of InsufficientFundsException
     * @throws Throwable
     */
    @Test(expected = InsufficientFundsException.class)
    public void testCreateTransfer() throws Throwable {
        LOGGER.trace("ExceptionHandlerTest.testCreateTransfer is initiated");

        // Arrange
        Transfer request = new Transfer(null, 100000.00, "1012345678", "2012345678");

        // Act
        controller.createTransaction(request);
    }

}
