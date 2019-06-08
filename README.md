# money-transfer-api

Its a REST API that demonstrates money transfer transaction between bank accounts.


Contents
--------
- [Architecture and Design Decisions](#architecture-and-design-decisions)
- [Building and running application](#building-and-running-application)
- [Endpoints](#endpoints)
- [Tests](#tests)
- [Static code analysis](#static-code-analysis)

Architecture and Design Decisions
---------------------------------

### Technologies

* Application: Java 8, Jersey, JAX-RS, Hibernate, JPA, HSQLDB, Jetty, Log4j, Maven
* Tests: Junit, Jococo, HSQLDB, Maven


### Architecture

This application uses Jersey which provides JAX-RS APIs to expose REST endpoints. In the backend it uses JPA, with hibernate as JPA provider, to perform operations on database. It also uses HSQL DB as an in memory database for the sake of simplicity. Jetty is used as an embedded web server to run the application as standalone.

### Multi Threading / Concurrency, Locking and DeadLock Prevention

#### Multi Threading
The application makes use of concurrency API from Java to fork new Thread from the Thread Pool upon new request. Hence multiple requests can be served parallely without waiting for previous request(s) to complete.

#### Locking
Since multiple requests are processed at the same time, database updates can happen parallely. This can lead to issues like lost update etc. PESSMISTIC_WRITE  locking technique is used to avoid lost update issues by acquiring PESSIMISTIC_WRITE lock on records to be updated.

#### Deadlock Prevention
Deadlock can happen as threads acquire one lock and wait for other lock. This is prevented by implementing formal deadlock prevention mechanism of acquiring locks in same order.

#### Thread Safety
The controller and service classes are implemented by following best practices of thread safe implementation like stateless nature, etc.

#### Scalability
This application can be deployed to multiple instances to cater high volumes of requests without any overheads like cache replication, inter-communication between the instances etc.

### Logging
Log4j is used for logging errors and other messages. All the logs are written to money-transfer.log . By default the logging level is info and can be changed as needed in log4j.properties

### Exception Handling
Critical Exceptions detailed below are handled. Other exceptions like negative amounts etc are not handled for the sake of simplicity. Other validations like mandatory check for account number and amount is also not handled.

#### Insufficient Funds
This exception occurs in a scenario when there are insufficient funds in senders account to do the transfer for the amount specified in the request.  A 412 error is returned to the client with details of the exception.

#### Missing Account
This happens when the application cannot find a sender or receiver account for account numbers specified in the request. A 404 error is returned to the client with details of the exception.


Building and running application
--------------------------------

### Prerequisites for building and running the application

* Java 8
* Maven 3+

### Building jar

To generate standalone jar file with all dependencies, execute:

```
mvn clean install
```
### Running

After executing the command above, to run server as a standalone jar, execute:

```
java -jar target\moneytransfer-1.0.0.jar
```

Server will start running on port `8080`

Note: To ease testing the application initializes the in memory database with below two accounts represented as JSON array.

```json
{
    "accounts": [
        {
            "accountNumber": "5012345678",
            "balance": 20000,
            "id": 1,
            "name": "Mike"
        },
        {
            "accountNumber": "6012345678",
            "balance": 5000,
            "id": 2,
            "name": "Bob"
        }
    ]
}
```

Endpoints
---------

| Endpoint         | Method | Payload          | Payload example                                                                                               | Return                                      |
|:-----------------|:-------|:-----------------|:--------------------------------------------------------------------------------------------------------------|:--------------------------------------------|
| /account        | POST   | name, account number, balance  | { "name": "Peter", "accountNumber": "7012345678", "balance": 5000.00 }       | `201 Created` with message "created account with id <>" |
| /account/id  | GET    | id           |                                                                                                               | `200 OK` with the existing account  |
| /accounts  | GET    |            |                                                                                                               | `200 OK` with all  existing accounts  |
| /account/id  | DELETE    | id           |                                                                                                               | `204 No Content` |           
| /transaction        | POST   | amount, senderAccountNumber, receiverAccountNumber  | { "amount" : 10000.00, "senderAccountNumber": "5012345678", "receiverAccountNumber": "6012345678" }       | `201 Created` with message "created transaction with id <>" |
| /transaction/id  | GET    | id           |                                                                                                               | `200 OK` with the existing transaction  |
| /transactions  | GET    |            |                                                                                                               | `200 OK` with all  processed transactions  |
| /transaction/id  | DELETE    | id           |                                                                                                               | `204 No Content` |  

Tests
-----

In order to execute **unit tests**, run the following command:

```
mvn clean test
```

Generated test report with coverage can be found in `target\site\jacoco\` directory

Static code analysis
--------------------

Static code analysis runs CheckStyle, PMD and SpotBugs (maintained successor of FindBugs). It can be executed with command:

```
mvn clean compile site
```

Generated reports can be found in `target\site\` directory
