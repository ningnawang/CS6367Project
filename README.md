# CS6367Project
This is the project for CS6367 Software Testing Verification Validation and Quality Assurance.
We will be working on Statement Coverage.


## Phase-1
An automated coverage collection tool that would collect the statement coverage for 10 real-world java projects (>1000 lines of code) with JUnit tests (>50 tests) from GitHub.

- How to build the project:
```
$ #From root folder
$ mvn clean package
```
- How to trigger javaagent for test project:
```
$java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar=<testing project name> -jar test/target/test-0.1-SNAPSHOT.jar
// example:
$java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar=test -jar test/target/test-0.1-SNAPSHOT.jar
```
- How to run maven test:
```
$mvn test
```
