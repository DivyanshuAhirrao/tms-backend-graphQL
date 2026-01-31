# TMS Backend - Spring Boot GraphQL API

## Technology Stack
- Java 21
- Spring Boot 3.2.1
- GraphQL (Spring GraphQL)
- Spring Data JPA / Hibernate
- H2 Database (In-Memory)
- Spring Security + JWT
- BCrypt Password Encryption
- Lombok
- Maven

## Prerequisites
1. **Java 21**: Download from https://www.oracle.com/java/technologies/downloads/#java21
2. **Maven 3.6+**: Download from https://maven.apache.org/download.cgi

### Windows Installation
```bash
# Install Java 21
winget install Oracle.JDK.21

# Verify installation
java -version

# Install Maven
# Download from maven.apache.org and extract to C:\Program Files\Apache\Maven
# Add to PATH: C:\Program Files\Apache\Maven\bin
mvn -version
```

## Setup & Run

### 1. Navigate to backend directory
```bash
cd backend
```

### 2. Install dependencies
```bash
mvn clean install
```

### 3. Run the application
```bash
mvn spring-boot:run
```

### 4. Access the application
- **GraphQL Endpoint**: http://localhost:8080/graphql
- **GraphiQL UI**: http://localhost:8080/graphiql
- **H2 Console**: http://localhost:8080/h2-console

## Default Test Users
```
Admin:
  Username: admin
  Password: admin123
  
Employee:
  Username: employee
  Password: emp123
```

## GraphQL API Examples

### Login
```graphql
mutation {
  login(username: "admin", password: "admin123") {
    token
    user {
      id
      username
      email
      role
    }
  }
}
```

### Get Shipments with Pagination
```graphql
query {
  shipments(page: {page: 0, size: 10}, sortBy: "createdAt", sortDirection: DESC) {
    content {
      id
      shipmentNumber
      shipperName
      carrierName
      status
      rate
    }
    totalElements
    totalPages
    currentPage
  }
}
```

### Create Shipment
```graphql
mutation {
  createShipment(input: {
    shipmentNumber: "SH-2024-016"
    shipperName: "Test Company"
    carrierName: "FedEx"
    pickupLocation: "New York"
    deliveryLocation: "Boston"
    status: PENDING
    rate: 150.00
  }) {
    id
    shipmentNumber
    status
  }
}
```

## Architecture
```
src/main/java/com/tms/
├── config/          # Configuration classes
├── controller/      # REST controllers (if needed)
├── dto/             # Data Transfer Objects
├── exception/       # Custom exceptions & handlers
├── model/           # JPA entities
├── repository/      # Spring Data repositories
├── resolver/        # GraphQL resolvers
├── security/        # Security & JWT configuration
└── service/         # Business logic layer
```

## Key Features
✅ Clean Architecture (Controller/Service/Repository)
✅ GraphQL API with Queries & Mutations
✅ JWT Authentication & Authorization
✅ Role-Based Access Control (ADMIN/EMPLOYEE)
✅ Pagination & Sorting
✅ Input Validation
✅ Global Exception Handling
✅ Logging with SLF4J
✅ Database Indexing for Performance
✅ Caching Support
✅ Transaction Management

## Database Schema
The H2 database initializes with sample data on startup. Check `data.sql` for 15 pre-loaded shipments.

## Performance Optimizations
- Database indexing on frequently queried fields
- JPA query optimization
- Connection pooling (HikariCP)
- Caching layer with Spring Cache
- Lazy loading for relationships

## Security
- BCrypt password hashing
- JWT token-based authentication
- Role-based authorization (@PreAuthorize)
- CORS configuration for frontend integration
- Stateless session management

## Port Configuration
Default port: 8080 (configurable in application.properties)
