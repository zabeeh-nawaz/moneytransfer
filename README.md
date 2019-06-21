# money-transfer-api (kotlin-tests branch)

This branch is created on top of test-parallel branch. This branch contains unit tests for negative scenarios. The tests are written in kotlin which invoke the app classes written in java.

Running Kotlin Tests
--------------------------------

### Prerequisites for building and running the application

* Java 8
* Maven 3+

### Running the tests

```
mvn clean test
```

Note: Tests are run against in memory database so no dedicated db setup and db connection setup is required.
