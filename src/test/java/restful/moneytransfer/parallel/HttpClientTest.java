package restful.moneytransfer.parallel;

import static com.restful.moneytransfer.App.LOGGER;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.restful.moneytransfer.model.Account;

public class HttpClientTest {

    private Double initialAmount = 10000.00;
    private Double transferAmount = 100.00;
    private String senderAccountNumber = "7012345678";
    private String receiverAccountNumber = "8012345678";
    private int senderId;
    private int receiverId;

    private CloseableHttpClient client = HttpClients.createDefault();


    @Before
    public void setUpTestData() throws ClientProtocolException, IOException {
        senderId = createAccount("Tom", senderAccountNumber, initialAmount);
        receiverId = createAccount("Angela", receiverAccountNumber, initialAmount);
    }

    @Test
    public void parallelInvocationTest() throws InterruptedException,
    ExecutionException, BrokenBarrierException, IOException {
        // Arrange
        int totalThreads = 10;
        ExecutorService executor = Executors
                .newFixedThreadPool(totalThreads);
        final CyclicBarrier gate = new CyclicBarrier(totalThreads + 1);
        Callable<CloseableHttpResponse> task1 = () -> {
            gate.await();
            HttpPost httpPost = new HttpPost("http://localhost:8080/transaction");
            String json = "{ \"amount\" : "
                    + transferAmount
                    + ", \"senderAccountNumber\": \""
                    + senderAccountNumber
                    + "\", \"receiverAccountNumber\": \""
                    + receiverAccountNumber
                    + "\" }";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            httpPost.releaseConnection();
            return response;
        };

        // Act
        ArrayList<Future<CloseableHttpResponse>> futureList = new ArrayList<>();
        for (int i = 0; i < totalThreads; i++) {
            Future<CloseableHttpResponse> future = null;
            future = executor.submit(task1);
            futureList.add(future);
        }
        gate.await();
        futureList.forEach((f) -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Exception while posting transaction from HttpClientTest " + e.getMessage());
            }
        });

        // Assert
        Account senderAccount = getAccount(senderId);
        Account receiverAccount = getAccount(receiverId);
        Double expectedSenderBalance = initialAmount - totalThreads * transferAmount;
        Double expectedReceiverBalance = initialAmount + totalThreads * transferAmount;
        assertEquals(senderAccount.getBalance(), expectedSenderBalance);
        assertEquals(receiverAccount.getBalance(), expectedReceiverBalance);
    }

    @After
    public void cleanTestData() throws ClientProtocolException, IOException {
        deleteAccount(senderId);
        deleteAccount(receiverId);

        client.close();
    }

    private void deleteAccount(int accountId) throws ClientProtocolException, IOException {
        HttpDelete httpDelete = new HttpDelete("http://localhost:8080/account/" + accountId);
        client.execute(httpDelete);
        httpDelete.releaseConnection();
    }

    private Account getAccount(int accountId) throws ClientProtocolException, IOException {
        HttpGet httpGet = new HttpGet("http://localhost:8080/account/" + accountId);
        ResponseHandler<Account> responseHandler = new ResponseHandler<Account>() {

            @Override
            public Account handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(responseString, Account.class);
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
        Account account = client.execute(httpGet, responseHandler);
        httpGet.releaseConnection();

        return account;
    }

    private int createAccount(String name, String accountNumber, double balance) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost("http://localhost:8080/account");
        String json = " { \"name\": \"" + name + "\", \"accountNumber\" : \"" + accountNumber + "\", \"balance\": " + balance + " }";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        httpPost.releaseConnection();
        String responseString = new BasicResponseHandler().handleResponse(response);

        return Integer.parseInt(responseString.substring(24));
    }

}
