package restful.moneytransfer.exception

import com.restful.moneytransfer.controller.MoneyTransferController
import com.restful.moneytransfer.entity.BankAccount
import com.restful.moneytransfer.exception.InsufficientFundsException
import com.restful.moneytransfer.exception.MissingResourceException
import com.restful.moneytransfer.model.Transfer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Test
import javax.persistence.Persistence
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Nawaz
 *
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExceptionHandlingTest {

    /**
     *  Instance of controller
     */
    private val subject = MoneyTransferController()

    /**
     *  Sets up initial data
     */
    @BeforeAll
    internal fun setup() {
        val emf = Persistence.createEntityManagerFactory("MoneyTransferPU")
        val em = emf.createEntityManager()

        em.getTransaction().begin()
        var accountOne = BankAccount("1012345678", "Tom", 20000.00)
        var accountTwo = BankAccount("2012345678", "Angella", 5000.00)
        em.persist(accountOne)
        em.persist(accountTwo)

        em.getTransaction().commit()
        em.close()
    }

    /**
     * Unit test to test throwing of InsufficientFundsException
     * @throws Throwable
     */
    @Test
    fun testCreateTransferForInsufficientFunds() {
        // Arrange
        var request = Transfer(null, 100000.00, "1012345678", "2012345678")

        // Act and Assert
        org.junit.jupiter.api.Assertions.assertThrows(InsufficientFundsException::class.java) {
            subject.createTransaction(request)
        }
    }
    
    /**
     * Unit test to test throw of InsufficientFundsException
     * @throws Throwable
     */
    @Test
    fun testCreateTransferForNonExistingAccount() {
        // Arrange
        var request = Transfer(null, 100000.00, "9999999999", "2012345678")

        // Act and Assert
        org.junit.jupiter.api.Assertions.assertThrows(MissingResourceException::class.java) {
            subject.createTransaction(request)
        }
    }

}