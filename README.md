
# Spring Boot Application with Docker and PostgreSQL

This project is a Spring Boot application that connects to a PostgreSQL database. The application and the database are containerized using Docker.

---

## **Prerequisites**
Make sure you have the following installed:
- Java 21 or later
- Maven
- Docker
- Docker Compose

---

## **How to Run the Project**

### 1. **Build the application**

Run the following Maven command to compile the project and package the application as a `.jar` file:

```bash
mvn clean package -DskipTests
```

This will skip the tests and generate the `app.jar` file in the `target` directory.

---

### 2. **Run the application with Docker Compose**

Once the build is complete, you can start the application and the PostgreSQL database using Docker Compose:

```bash
docker-compose up
```

This command will:
- Build the application container.
- Start both the application and PostgreSQL containers.

The application will be accessible at `http://localhost:8080`.

---

## **How to Run Tests**

To run the unit and integration tests, use the following command:

```bash
mvn clean test
```

This command will execute all unit and integration tests.

---

## **Stopping the Application**

To stop the application and remove all running containers, use:

```bash
docker-compose down
```

---

## Accessing Swagger UI

The application provides a Swagger UI interface that allows easy testing of the available API endpoints.

Swagger UI can be accessed at:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Simply open the above link in your browser to view the full API documentation and test the available methods.

---

## Default Credentials

The default login credentials for the application are:

- **Username:** `user`
- **Password:** `password`

These can be used to authenticate and access the application with basic authentication.