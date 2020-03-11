# CS6367Project
This is the project for CS6367 Software Testing Verification Validation and Quality Assurance.
We will be working on Statement Coverage.


## Phase-1
An automated coverage collection tool that would collect the statement coverage for 10 real-world java projects (>1000 lines of code) with JUnit tests (>50 tests) from GitHub.

- How to build the project:
```
$ #From root folder
$ mvn package
```
- How to test run the newly compiled JAR:
```
$ cd asm
$ java -cp target/agent-0.1-SNAPSHOT.jar com.courseproject.app.App
```
- How to run maven test:
```
$cd asm
$mvn test
```
