# Hexagonal Orders Application

## Overview
This project demonstrates a Spring Boot application implementing the Hexagonal Architecture (also known as Ports and Adapters). The application manages orders and order items, showcasing clean architecture principles and separation of concerns.

## Project Structure
```
src/main/java/com/example/hexagonalorders/
├── domain/                    # Domain Layer (Core Business Logic)
│   └── model/                # Domain Entities
│       ├── Order.java
│       └── OrderItem.java
├── application/              # Application Layer (Use Cases & Ports)
│   ├── port/                # Ports (Interfaces)
│   │   ├── in/             # Input Ports (Use Cases)
│   │   │   └── OrderUseCase.java
│   │   └── out/            # Output Ports (Repositories, Services)
│   │       ├── OrderRepository.java
│   │       └── OrderNumberGenerator.java
│   └── service/            # Application Services
│       └── OrderService.java
├── adapter/                 # Adapter Layer (Infrastructure)
│   ├── in/                 # Input Adapters (Primary/Driving)
│   │   └── web/           # Web Adapters
│   │       ├── OrderController.java
│   │       ├── dto/       # Data Transfer Objects
│   │       │   ├── OrderDto.java
│   │       │   └── OrderItemDto.java
│   │       └── mapper/    # DTO Mappers
│   │           └── OrderMapper.java
│   └── out/               # Output Adapters (Secondary/Driven)
│       ├── persistence/   # Persistence Adapters
│       │   ├── OrderJpaEntity.java
│       │   ├── OrderItemJpaEntity.java
│       │   ├── JpaOrderRepository.java
│       │   └── OrderRepositoryAdapter.java
│       └── orderNumber/   # Order Number Generation Adapters
│           └── UuidOrderNumberGenerator.java
└── HexagonalOrdersApplication.java
```

## Technologies Used
- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- Lombok
- Maven

## Getting Started
1. Clone the repository
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `DELETE /api/orders/{id}` - Delete an order

## H2 Database Console
The application uses H2 as an in-memory database. You can access the H2 console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (empty)

## Hexagonal Architecture Benefits
This project implements Hexagonal Architecture (Ports and Adapters) which provides several benefits:
1. **Domain-Centric Design**: The core business logic is isolated from external concerns
2. **Clear Boundaries**: The architecture enforces clear boundaries between layers
3. **Testability**: Each layer can be tested independently
4. **Flexibility**: External dependencies can be easily swapped (e.g., database, UI)
5. **Maintainability**: Changes in one layer don't affect others

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request