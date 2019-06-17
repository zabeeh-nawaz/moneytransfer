# money-transfer-api (test-parallel branch)

This branch is created on top of master branch. This branch contains integration test which simulates concurrent invocation of the money-transfer api by multiple threads. The test asserts that no update is lost despite simultaneous invocations. Lost update most likely happens in systems where concurrency is not handled properly. Through lost update assertion the test also checks the thread safe nature of the application.

In this branch tests are executed against MySQL database as some inconsistencies were encountered in the locking mechanism of HSQL in memory database.

Test Threads Initialization
--------------------------------
Though invocation of all threads at the same time is not possible in a single processor machine, we can ensure that a thread does not start while other threads are being initialized. This is achieved through synchronization with the help of CyclicBarrier. CyclicBarrier ensures that all threads wait until the barrier value is reached. 

The barrier value is set to number of threads + 1

```
final CyclicBarrier gate = new CyclicBarrier(totalThreads + 1);
```

When the thread is first created and initialized, it is made to wait until all the threads are initialized through below code 

```
gate.await();
```

Once all the threads are initialized, gate.await() is called from the main thread which will break the barrier and all the waiting threads are notified to resume their execution.


Running Integration Tests
--------------------------------

### Prerequisites for building and running the application

* Java 8
* Maven 3+
* MySql

### Update persistence.xml

Update MySQL DB connection details like hostname, port, username and password.

```
Path: src/main/resources/META-INF/persistence.xml
```

### Building jar

To generate standalone jar file with all dependencies, execute:

```
mvn clean install
```

### Running the application

As the test will run against the deployed application, execute below command to start the application as standalone:

```
java -jar target\moneytransfer-1.0.0.jar
```

Server will start running on port `8080`

### Running the test

```
mvn test -Dtest=restful.moneytransfer.parallel.*Test
```

Note: By default the test runs with 10 concurrent threads. The number of threads can be increase or decreased by updating the value for the variable totalThreads in HttpClientTest class
