# WebDev 2 — Week 2: Spring Framework & Spring Boot Fundamentals

**Term:** Prelim · **Week:** 2  
**Course:** WebDev 2  
**Student:** geoxify (`geoffbenedict.carino@lorma.edu`)  
**Repository:** `webdev2`  
**Branch:** `week2`  

---

## 📌 Table of Contents
1. [Overview & Learning Outcomes](#-overview--learning-outcomes)
2. [Project Structure](#-project-structure)
3. [Implementation Details (Tasks 1–5)](#%EF%B8%8F-implementation-details)
   - [Task 1: Bootstrap the Spring Boot Project](#task-1--bootstrap-the-spring-boot-project-easy)
   - [Task 2: Service & Repository Beans](#task-2--service--repository-beans-easy-medium)
   - [Task 3: Constructor Dependency Injection](#task-3--constructor-dependency-injection-medium)
   - [Task 4: Externalized Application Configuration](#task-4--externalized-application-configuration-medium)
   - [Task 5: Startup Wiring Report](#task-5--startup-wiring-report-hard)
4. [Spring IoC Object Dependency Architecture](#-spring-ioc-object-dependency-architecture)
5. [Build & Execution Guide](#-build--execution-guide)
6. [Port Configuration](#-port-configuration)
7. [Sample Startup Report Output](#-sample-startup-report-output)
8. [Evidence & Screenshots](#-evidence--screenshots)
9. [Acceptance Criteria Checklist](#-acceptance-criteria-checklist)

---

## 🎯 Overview & Learning Outcomes
This project transitions the baseline Java application from Week 1 into an enterprise-grade Spring Boot application. It applies fundamental Spring Framework patterns including the Inversion of Control (IoC) container, declarative bean definitions via stereotype annotations, constructor-based dependency injection, externalized configuration binding, and automated startup execution runners.

Key concepts demonstrated:
- **Spring Initializr Bootstrapping:** Generating a Spring Boot project with Maven and Spring Web.
- **Stereotype Annotations:** Registering Spring-managed beans using `@Repository` and `@Service`.
- **Constructor-Based Dependency Injection:** Decoupling component lifecycle management to the Spring IoC container without manually invoking `new`.
- **Externalized Configuration:** Binding properties from `application.properties` into typed POJO beans using `@ConfigurationProperties(prefix = "shop")`.
- **Startup Lifecycle Hooks:** Executing automated startup reporting with `CommandLineRunner`.

---

## 📁 Project Structure

```text
webdev2/
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── screenshot/
│   ├── task1_bootstrap_port8082.png
│   ├── task3_constructor_injection.png
│   └── task5_startup_report.png
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── webdev2/
│   │   │               ├── Product.java
│   │   │               ├── ProductReportRunner.java
│   │   │               ├── ProductRepository.java
│   │   │               ├── ProductService.java
│   │   │               ├── ShopProperties.java
│   │   │               └── Webdev2Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── webdev2/
│                       └── Webdev2ApplicationTests.java
```

---

## 🛠️ Implementation Details

### Task 1 — Bootstrap the Spring Boot Project _(Easy)_
- Initialized the project with Maven, Java 17, and `spring-boot-starter-webmvc` (`Spring Web`).
- Configured the embedded HTTP server to listen on port `8082` via `src/main/resources/application.properties`:
  ```properties
  server.port=8082
  ```
- Verified successful bootstrap and clean execution with no startup errors.

### Task 2 — Service & Repository Beans _(Easy–Medium)_
- **`Product.java`**: Domain model encapsulating `name` (String) and `price` (double) with parameterized constructor, standard getters/setters, and `toString()`.
- **`ProductRepository.java`**: Annotated with `@Repository`. Manages an in-memory `List<Product>` containing:
  - Laptop (45,000)
  - Mouse (800)
  - Keyboard (1,500)
  - Monitor (12,000)
  - Headset (2,500)
  - Printer (8,500)
  Exposes `findAll()` to retrieve the catalog without connecting to a database.
- **`ProductService.java`**: Annotated with `@Service`. Implements business logic filtering via `getProductsAboveThreshold(double threshold)` using Java Streams.

### Task 3 — Constructor Dependency Injection _(Medium)_
- Wired `ProductRepository` into `ProductService` strictly via **constructor injection**:
  ```java
  @Service
  public class ProductService {
      private final ProductRepository productRepository;

      public ProductService(ProductRepository productRepository, ShopProperties shopProperties) {
          this.productRepository = productRepository;
          this.shopProperties = shopProperties;
      }
      // ...
  }
  ```
- Eliminates manual object creation (`new ProductRepository()`), shifting lifecycle responsibility entirely to the Spring IoC container.

### Task 4 — Externalized Application Configuration _(Medium)_
- Externalized application metadata into `src/main/resources/application.properties`:
  ```properties
  server.port=8082

  shop.name=My Web Store
  shop.currency=PHP
  ```
- Created `ShopProperties.java` annotated with `@Component` and `@ConfigurationProperties(prefix = "shop")` to type-safely bind `shop.name` and `shop.currency`.
- Enabled configuration binding in `Webdev2Application.java` using `@EnableConfigurationProperties(ShopProperties.class)`.
- Injected `ShopProperties` into `ProductService` via constructor injection so shop values are never hardcoded.

### Task 5 — Startup Wiring Report _(Hard)_
- Created `ProductReportRunner.java` implementing `CommandLineRunner` and registered it as a Spring `@Component`.
- Injected `ProductService` into `ProductReportRunner` via constructor injection.
- Automatically executes upon Spring Boot startup to output the formatted catalog report for items exceeding PHP 5,000.

---

## 🏛️ Spring IoC Object Dependency Architecture

```text
Spring IoC Container
        │
        ├── ProductRepository
        │
        ├── ShopProperties
        │
        ├── ProductService
        │       │
        │       ├── ProductRepository
        │       └── ShopProperties
        │
        └── CommandLineRunner (ProductReportRunner)
                │
                └── ProductService
```

---

## 🚀 Build & Execution Guide

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Maven / Maven Wrapper (`mvnw.cmd` included in repository)

### 1. Build the Application
To compile sources and package the executable JAR:
```powershell
.\mvnw.cmd clean package
```

### 2. Run the Application
You can run the application using either the Spring Boot Maven plugin or directly with `java`:

**Option A — Using Maven Wrapper:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Option B — Running the packaged JAR:**
```powershell
java -jar target/webdev2-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Port Configuration
The application is configured to run on port **`8082`**:
- Configured in: `src/main/resources/application.properties`
- Setting: `server.port=8082`
- Verification: Startup log displays `Tomcat started on port 8082 (http) with context path '/'`.

---

## 📊 Sample Startup Report Output

When the application starts, the `ProductReportRunner` bean generates the following startup report:

```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.1.2)

2026-11-11T16:20:02.716+08:00  INFO 3928 --- [webdev2] [           main] com.example.webdev2.Webdev2Application   : Starting Webdev2Application v0.0.1-SNAPSHOT using Java 24.0.2 with PID 3928
2026-11-11T16:20:03.680+08:00  INFO 3928 --- [webdev2] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8082 (http)
2026-11-11T16:20:04.040+08:00  INFO 3928 --- [webdev2] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8082 (http) with context path '/'
2026-11-11T16:20:04.067+08:00  INFO 3928 --- [webdev2] [           main] com.example.webdev2.Webdev2Application   : Started Webdev2Application in 1.77 seconds

================================
       PRODUCT REPORT
================================
Shop: My Web Store
Currency: PHP

Products above PHP 5000:

Laptop - PHP 45000
Monitor - PHP 12000
Printer - PHP 8500
================================
```

---

## 📷 Evidence & Screenshots

| Screenshot | Description | File Path |
|---|---|---|
| **Task 1: Bootstrap & Port 8082** | Spring Boot application booting successfully on port 8082 with no errors. | `screenshot/task1_bootstrap_port8082.png` |
| **Task 3: Constructor Injection** | Successful bean wiring and startup verification of constructor-injected beans. | `screenshot/task3_constructor_injection.png` |
| **Task 5: Startup Product Report** | Full console output of the `CommandLineRunner` wiring report. | `screenshot/task5_startup_report.png` |

---

## ✅ Acceptance Criteria Checklist

- [x] **Task 1:** Continued in existing `webdev2` repository on `week2` branch. Spring Boot project configured with Maven, Java 17, and Spring Web. Successfully boots on port `8082`.
- [x] **Task 2:** `ProductRepository` annotated with `@Repository`; `ProductService` annotated with `@Service`; products managed via in-memory `List<Product>`; price threshold filtering operates accurately.
- [x] **Task 3:** `ProductService` receives `ProductRepository` through its constructor. No `new ProductRepository()` calls exist inside `ProductService`.
- [x] **Task 4:** `shop.name` and `shop.currency` defined in `application.properties` and bound to typed `@ConfigurationProperties(prefix = "shop")` class (`ShopProperties`), injected into `ProductService`.
- [x] **Task 5:** `CommandLineRunner` (`ProductReportRunner`) executes at application startup, printing the shop metadata and filtered products using Spring IoC-managed beans.
