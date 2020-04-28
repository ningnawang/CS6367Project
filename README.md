# CS6367Project
This is the project for CS6367 Software Testing Verification Validation and Quality Assurance.
We will be working on Statement Coverage.


## Phase-1
An automated coverage collection tool that would collect the statement coverage for 10 real-world java projects (>1000 lines of code) with JUnit tests (>50 tests) from GitHub.

### For production
1. pull all sample projects:
```
$ git submodule update --init
```

2. build agent
```
$ cd agent
$ mvn clean package
```

3. go to any project want to test, add following into pom.xml
```
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<version>3.0.0-M4</version>
<configuration>
  <argLine>-javaagent:<hard-path/agent/target/agent-0.1-SNAPSHOT.jar>=<test-package-name></argLine>
  <properties>
    <property>
      <name>listener</name>
      <value>agent.CommonListener</value>
    </property>
  </properties>
</configuration>
</plugin>
```

For example, to run agent under test of project commons-dbutils, you should add:
```
    <argLine>-javaagent:/Users/NingnaWang/ninwang/utd/2020spring/CS6367Project/agent/target/agent-0.1-SNAPSHOT.jar=org/apache/commons/dbutils</argLine>
```

4. under project, run
```
$ mvn clean test
```

### For test
- How to build the project:
```
$ #From root folder
$ mvn clean package
```
- How to trigger javaagent for test project:
```
$java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar=<testing project name> -jar test/target/test-0.1-SNAPSHOT.jar
// example:
$ java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar=test -jar test/target/test-0.1-SNAPSHOT.jar
```
- How to run maven test:
```
$ mvn test
```

### For running forked test
```
$ mvn -T 6 surefire:test
```

## Phase 2

### How to build
From root directory:
```
$cd traceAgent
$mvn clean package
```
### How to run
Just like for running the statement listener, add the following plugin to the pom.xml of the project you want to trace invariants for:

```
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<version>3.0.0-M4</version>
<configuration>
  <argLine>-javaagent:<hard-path/agent/target/agent-0.1-SNAPSHOT.jar>=<test-package-name></argLine>
  <properties>
    <property>
      <name>listener</name>
      <value>invariantsAgent.InvariantsRunListener</value>
    </property>
  </properties>
</configuration>
</plugin>
```
Then, go to the root of the project directory and run:
```
$mvn clean test
```
