# Apache Camel Presentation

This is a presentation for an introductory look at Apache Camel.

## Running examples
Prior to running any examples, you must fill out the following configurations with the appropriate information:
- `src/main/resources/twitterapi.properties`
- `src/main/resources/gmail.properties`

Maven and Java must also be configured to run the examples.

### Java DSL Example
To run the `TwitterRoute.java` class on a Unix machine, use the following command from the top level directory:
- `mvn -e compile exec:java -Dexec.mainClass=TwitterRoute`

### Spring DSL Example
To run the `spring.xml` example on a Unix machine, run the following command from the top level directory:
- `mvn camel:run`
