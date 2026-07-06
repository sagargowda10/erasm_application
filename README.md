# ERASM – Enterprise Resource Allocation & Skill Management System

## Overview

ERASM is an Enterprise Resource Allocation & Skill Management System developed using Java Spring Boot.

The system helps organizations efficiently manage employees, projects, skills, certifications, resource requests, and resource allocation through secure REST APIs protected with JWT Authentication and Role-Based Access Control (RBAC).

---

## Features

- User Authentication (JWT)
- Role-Based Access Control (RBAC)
- Employee Management
- Skill Management
- Employee Skill Profile
- Certification Management
- Project Management
- Resource Request Workflow
- Resource Allocation
- Utilization Dashboard
- Audit Logging
- Reports
- Global Exception Handling
- Swagger API Documentation

---

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL

### Testing
- JUnit 5
- Mockito
- MockMvc
- JaCoCo

### Documentation
- Swagger / OpenAPI
- Postman

### Tools
- Maven
- Git
- GitHub
- STS (Spring Tool Suite)

---

## Project Structure

```
src/
database/
docs/
postman/
pom.xml
README.md
```

---

## Roles

- ADMIN
- DELIVERY_MANAGER
- RESOURCE_MANAGER
- EMPLOYEE
- AUDITOR

---

## Authentication

The application uses

- JWT Authentication
- BCrypt Password Encryption
- Spring Security
- Role-Based Authorization

---

## Database

MySQL 8

Database Script

```
database/
```

---

## API Documentation

Swagger

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI

```
http://localhost:8080/v3/api-docs
```

---

## Running the Project

Clone Repository

```bash
git clone https://github.com/sagargowda10/erasm_application.git
```

Go inside project

```bash
cd erasm
```

Run

```bash
mvn spring-boot:run
```

---

## Testing

Run

```bash
mvn test
```

Coverage

- JUnit 5
- Mockito
- JaCoCo
- 111 Tests
- 83%+ Code Coverage

---

## Documentation

Project Documentation

```
docs/ERASM_Documentation.docx
```

Testing Report

```
docs/ERASM_Testing_Report.docx
```

Postman Collection

```
postman/ERASM_API.postman_collection.json
```

---

## Git Workflow

- main
- develop
- feature/*
- release/*
- hotfix/*

---

## Author

Sagar N