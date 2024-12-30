# Debugger Project

A robust debugger project designed to compile and debug Java code seamlessly. The project integrates a **JVM runner** for executing code, powered by **Vert.x** and **Spring Boot**, ensuring high performance and scalability.

---

## 🚀 Features

- **JVM Runner**: Dynamically runs Java Virtual Machine for code execution.
- **Code Compilation**: Efficiently compiles Java code on-the-fly using the `CodeExecutionService`.
- **Debugging Support**: Provides debugging capabilities for analyzing and troubleshooting code.
- **Scalable Architecture**: Built with Vert.x for reactive programming and Spring Boot for dependency injection and REST APIs.
- **API-Driven**: Exposes RESTful endpoints for compiling and debugging code.
- **Modular Design**: Decoupled services for better maintainability and scalability.

---

## 🛠️ Tech Stack

### Backend
- **Vert.x**: Reactive toolkit for JVM applications.
- **Spring Boot**: For building REST APIs and managing the application's lifecycle.
- **Java**: Core language for code execution and debugging logic.

### Other Tools
- **Maven**: Dependency management and build automation.
- **JVM**: For running the compiled Java code.

---

## 🏗️ Architecture

### Components:
1. **CodeExecutionService**: Handles code compilation and debugging requests.
2. **JVM Runner**: Executes Java code within an isolated JVM instance.
3. **REST API**: Provides endpoints for triggering compilation and debugging workflows.

---

## 📦 Installation

### Prerequisites
- **Java 17 or above**
- **Maven 3.6+**
- **Docker** (optional, for containerized deployment)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/<username>/<repository-name>.git
   cd <repository-name>
   ```
2. Build the project:
  ```bash
  mvn clean install
```
3. Run the project:
```bash
mvn spring-boot:run
```
4. Access the application:

Base URL: http://localhost:8080
