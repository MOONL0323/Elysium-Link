# Elysium-Link  Deployment process of backend service system

## Pull code from github:

```shell
git clone git@github.com:MOONL0323/Elysium-Link.git
```

## Deployment Environment:

```shell
cd .\src\main\resources\
docker compose-up -d 
```

## Create table

Run the mysql.sql script in Resources to establish database information

## Introducing Meituan open source unique ID generator——Leaf

```shell
cd dm
git clone git@github.com:Meituan-Dianping/Leaf.git
git checkout feature/spring-boot-starter
cd leaf
mvn clean install -DskipTests
```

The specific configuration details can be found in the official document. The configuration has been completed here. You only need to add the package manually.

## Run the springboot program

Maven startup project:

```shell
mvn spring-boot:run
```

## Description of main directory structure of the project

#### directory tree:

dm-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── DemoApplication.java
│   │   │               ├── auditingService/
│   │   │               ├── comments/
│   │   │               ├── config/
│   │   │               ├── content/
│   │   │               ├── countService/
│   │   │               ├── finkcdc/
│   │   │               ├── interceptor/
│   │   │               ├── messagepush/
│   │   │               ├── test/
│   │   │               ├── userRelationships/
│   │   │               ├── util/
│   │   │               └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       ├── docker-compose.yml
│   │       ├── .env
│   │       └── mysql.sql
├── pom.xml
└── README.md

---

#### Module explanation:

**DemoApplication.java**: 

the entry class of the Spring Boot application, which contains the main method to start the application.

**auditingService/:**  

Audit Center
**comments/**:  

The comment module handles the business logic and data operations related to comments.

**config/**:  

Configuration module, including application configuration class.
**content/**: 

 The content management module handles content related business logic and data operations.

**countService/**:  

The counting service module handles the business logic and data operations related to counting.
**Finkcdc/:** 

Flink CDC module, which handles Flink CDC related functions, mainly subscribing to binlog to synchronize different data upstream and downstream.

**interceptor/**:  

Interceptor module, which handles the functions related to request interception.
**messagepush/**: 

 The message push module handles the business logic and data operations related to message push.

**test/:**  

Test module, including test code.

**userRelationships**/:  

The user relationship module handles business logic and data operations related to user relationships.

**util/:**  

Tool class module, providing general functions.

**resources/**: 

 Resource file directory, including applied configuration files and other resource files.

**Application.yml**: 

the main configuration file of the Spring Boot application.

**application-dev.yml:**  

The configuration file of the development environment.

**application-test.yml**: 

 The configuration file of the test environment.

**Docker-compose.yml:**

Docker Compose configuration file.

**. env**: 

nvironment variable configuration file.

**Mysql.sql:** 

SQL script files related to MySQL database.

**Pom.xml:** 

Maven project configuration file, which defines project dependencies and build configurations.

**README.md:**  

Project description document, which provides the basic information and instructions of the project.
Basically, each module is divided into controller, server, dao and entity to represent the controller (interacting with the front end), service layer (core processing code logic), persistence layer (dealing with the database) and some data entity classes defined to facilitate efficient data transmission between layers.

