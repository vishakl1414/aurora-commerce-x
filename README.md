# Aurora-Commerce X
E-Commerce Backend built with Java + Spring Boot

## Phase 1 — Monolithic Backend ✅
Built with Java 17 + Spring Boot 3.5, Maven, MySQL 8.0

### Tech Stack
- Java 17 + Spring Boot 3.5
- MySQL + Spring Data JPA
- JWT Security
- Stripe Payment
- Docker

### Features
- User Authentication (Register/Login)
- JWT Token Security
- Product Management (CRUD + Search + Filter)
- Order Management
- Stripe Payment Integration
- Dockerized Application

---

## Phase 2 — Microservices Architecture ✅
Built with Java 17 + Spring Boot 3.5.11, Gradle

### Services
| Service | Port | Description |
|---|---|---|
| Eureka Server | 8761 | Service Discovery |
| User Service | 8082 | User Registration & Management |
| Product Service | 8084 | Product CRUD, Search, Filter |
| API Gateway | 8080 | Single Entry Point |
| Order Service | 8085 | Order Management |
| RabbitMQ | 5672 | Message Broker |

### Tech Stack
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Data JPA
- MySQL 8.0
- RabbitMQ (Message Broker)
- Docker

### Features
- Service Discovery (Eureka)
- API Gateway Routing
- User Service (Register, Get Users)
- Product Service (CRUD, Search, Filter)
- Order Service (Place, Update, Cancel)
- RabbitMQ Messaging between Services
