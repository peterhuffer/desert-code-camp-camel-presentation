# Apache Camel Presentation

This is a presentation for an introductory look at Apache Camel.

## Running examples
Prior to running any examples, you must fill out the `src/main/resources/twitterapi.properties` and `src/main/resources/gmail.properties` configurations with the appropriate information.

### Java DSL Example
To run the `TwitterSource.java` class on a Unix machine, use the following command from the top level directory:
`mvn -e compile exec:java -Dexec.mainClass=TwitterRoute`

### Spring DSL Example
To run the `spring.xml` example on a Unix machine, run the following command from the top level directory:
`mvn camel:run`
