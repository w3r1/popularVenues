Requirements
maven 3.3.9 or higher, JDK1.8

Building and starting
mvn clean install
java -jar target/popularvenues-0.0.1-SNAPSHOT.jar

Endpoint reachable from
localhost:8080/v1/venues/popular?near=istanbul
Twitter clientid and secret is given in application.properties (test account with test email)

Not achieved in timeboxed 3-4 hour work
- Hystrix circuit breaker
- Swagger docs (wanted to autocreate them)
- Integration tests on wire with RestAssured
