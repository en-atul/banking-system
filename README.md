# Banking System Microservices with JWT Authentication

This project implements a microservices banking system with centralized JWT authentication at the API Gateway level.

## 🚀 Quick Start

### Entry Point
**All API requests must go through the API Gateway at:**
```
http://localhost:8765
```

**⚠️ Important:** Do NOT access services directly. Always use the API Gateway as the single entry point.

### Service Discovery
- **Eureka Server**: http://localhost:8761/
- **Kafka UI**: http://localhost:4002/
- **Kafdrop**: http://localhost:9000/

## Architecture Overview

### Authentication Flow
1. **API Gateway** acts as the central authentication point
2. **Auth Service** handles user registration, login, and token generation
3. **Shared JWT Utility** provides common JWT validation logic
4. **All other services** receive pre-validated requests with user information in headers

### Security Implementation

#### API Gateway Authentication Filter
- **Global Filter**: Intercepts all requests before routing
- **Public Endpoints**: `/auth/**`, `/actuator/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- **Token Validation**: Validates JWT signature and expiration
- **Role-Based Access Control**: Enforces role requirements per endpoint
- **User Information**: Adds user details to headers for downstream services

#### Role-Based Access Control
- **Account Service**: Requires `USER` or `ADMIN` role
- **Customer Service**: Requires `USER` or `ADMIN` role  
- **Transaction Service**: Requires `USER` or `ADMIN` role
- **Ledger Service**: Requires `ADMIN` role only
- **Notification Service**: Requires `USER` or `ADMIN` role

#### Error Responses
The API Gateway returns standardized error responses:
```json
{
  "error": "UNAUTHORIZED",
  "message": "Access token is missing or invalid",
  "path": "/account/123",
  "timestamp": "2024-01-15T10:30:00",
  "status": 401
}
```

### Shared JWT Utility

The `shared-jwt-util` module provides:
- `JwtClaims`: DTO for token claims
- `JwtValidator`: Interface for token validation
- `AbstractJwtValidator`: Base implementation with common parsing logic
- `AuthErrorResponse`: Standardized error response format

### Service-Specific Implementations

#### Auth Service
- Uses `AuthServiceJwtValidator` with database validation
- Validates tokens against stored token records
- Handles token refresh and cleanup

#### API Gateway  
- Uses `GatewayJwtValidator` with signature-only validation
- No database dependency for performance
- Validates token structure and expiration

## 🏗️ Building and Running

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose

### Configuration Setup

**⚠️ IMPORTANT: External Configuration Repository Required**

This project uses centralized configuration managed via [Spring Cloud Config](https://spring.io/projects/spring-cloud-config) from an external repository:

**Configuration Repository**: [https://github.com/en-atul/banking-system-config](https://github.com/en-atul/banking-system-config)

#### Option 1: Use Remote GitHub Repository (Recommended)
The config server is pre-configured to fetch configuration from the remote GitHub repository. No additional setup required.

#### Option 2: Clone and Use Local Repository
If you want to modify configurations locally:

1. **Clone the config repository:**
   ```bash
   git clone https://github.com/en-atul/banking-system-config.git
   cd banking-system-config
   ```

2. **Update config server path** in `config-server/src/main/resources/application.properties`:
   ```properties
   spring.cloud.config.server.git.uri=file:///path/to/your/local/banking-system-config
   ```

3. **Restart config server** after making changes

#### Configuration Files Available
The external repository contains configuration for all services:
- `account-service.properties` / `account-service-dev.properties`
- `auth-service.properties` / `auth-service-dev.properties`
- `customer-service.properties` / `customer-service-dev.properties`
- `notification-service.properties` / `notification-service-dev.properties`
- `transaction-service.properties` / `transaction-service-dev.properties`
- `ledger-service.properties` / `ledger-service-dev.properties`

### Build Order
1. Build shared-jwt-util first:
   ```bash
   cd shared-jwt-util
   mvn clean install
   ```

2. Build all services:
   ```bash
   mvn clean install
   ```

## 🚀 Step-by-Step Application Startup

### ⚠️ **IMPORTANT: Docker is Essential**

**Docker must be started FIRST** because it provides all the required infrastructure services:
- **PostgreSQL** - Database for Account and Customer services
- **MongoDB** - Database for Notification service  
- **Kafka** - Message broker for asynchronous communication
- **Zookeeper** - Required for Kafka cluster management
- **Kafka UI** - Web interface for Kafka monitoring
- **Zipkin** - Distributed tracing system
- **Eureka Server** - Service discovery and registry

### Step 1: Start Infrastructure Services

**For Development:**
```bash
# Start all infrastructure services (PostgreSQL, MongoDB, Kafka, Zookeeper, Eureka, Zipkin)
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up
```

**For Production:**
```bash
# Start production infrastructure
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

**Verify Infrastructure is Running:**
```bash
# Check if all containers are running
docker ps

# Expected containers:
# - postgres (PostgreSQL database)
# - mongo (MongoDB database)
# - zookeeper (Kafka dependency)
# - kafka (Message broker)
# - kafka-ui (Kafka monitoring)
# - kafdrop (Alternative Kafka UI)
# - zipkin (Distributed tracing)
```

### Step 2: Verify Infrastructure Services

**Check Service Health:**
- **Eureka Server**: http://localhost:8761/ (should show empty service list initially)
- **Kafka UI**: http://localhost:4002/ (should show Kafka cluster)
- **Kafdrop**: http://localhost:9000/ (alternative Kafka UI)
- **Zipkin**: http://localhost:9411/ (distributed tracing)

### Step 3: Start Microservices in Order

**⚠️ Start services in this specific order:**

1. **Config Server** (Port: 8888)
   ```bash
   cd config-server
   mvn spring-boot:run
   ```
   - Provides centralized configuration from external repository
   - Fetches config from [https://github.com/en-atul/banking-system-config](https://github.com/en-atul/banking-system-config)
   - Other services depend on this for configuration

2. **Naming Server/Eureka** (Port: 8761)
   ```bash
   cd naming-server
   mvn spring-boot:run
   ```
   - Service discovery registry
   - All services register here

3. **API Gateway** (Port: 8765)
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```
   - Main entry point for all API requests
   - Requires Eureka for service discovery

4. **Auth Service** (Port: 8000)
   ```bash
   cd services/auth-service
   mvn spring-boot:run
   ```
   - Handles user authentication and JWT tokens
   - Required for all protected endpoints

5. **Account Service** (Port: 8001)
   ```bash
   cd services/account-service
   mvn spring-boot:run
   ```
   - Manages bank accounts
   - Requires Kafka for notifications

6. **Customer Service** (Port: 8002)
   ```bash
   cd services/customer-service
   mvn spring-boot:run
   ```
   - Manages customer information

7. **Notification Service** (Port: 8500)
   ```bash
   cd services/notification-service
   mvn spring-boot:run
   ```
   - Handles notifications via Kafka
   - Requires MongoDB and Kafka

8. **Transaction Service** (Port: 8003)
   ```bash
   cd services/transaction-service
   mvn spring-boot:run
   ```
   - Manages financial transactions

9. **Ledger Service** (Port: 8004)
   ```bash
   cd services/ledger-service
   mvn spring-boot:run
   ```
   - Manages ledger entries (Admin only)

### Step 4: Verify All Services are Running

**Check Eureka Dashboard**: http://localhost:8761/
- Should show all services registered
- All services should have status "UP"

**Check API Gateway Health**: http://localhost:8765/actuator/health
- Should return healthy status

**Test Authentication Flow**:
```bash
# Register a user
curl -X POST http://localhost:8765/auth/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com", 
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Infrastructure Services Overview

| Service | Port | URL | Description | Status Check |
|---------|------|-----|-------------|--------------|
| **API Gateway** | 8765 | http://localhost:8765 | Main entry point for all API requests | `/actuator/health` |
| **Eureka Server** | 8761 | http://localhost:8761/ | Service discovery and registry | Dashboard |
| **Config Server** | 8888 | http://localhost:8888 | Centralized configuration | `/actuator/health` |
| **Kafka UI** | 4002 | http://localhost:4002/ | Kafka cluster monitoring and management | Dashboard |
| **Kafdrop** | 9000 | http://localhost:9000/ | Alternative Kafka UI | Dashboard |
| **Zipkin** | 9411 | http://localhost:9411/ | Distributed tracing system | Dashboard |
| **PostgreSQL** | 5336 | localhost:5336 | Account and Customer data | `docker ps` |
| **MongoDB** | 27017 | localhost:27017 | Notification data | `docker ps` |
| **Kafka** | 9092 | localhost:9092 | Message broker | Kafka UI |
| **Zookeeper** | 2181 | localhost:2181 | Kafka dependency | `docker ps` |

### 🚨 **Common Startup Issues**

1. **"Connection refused" errors**
   - Ensure Docker containers are running: `docker ps`
   - Check if ports are available: `lsof -i :PORT`

2. **"Service not found" in Eureka**
   - Verify Config Server is running first
   - Check service registration logs
   - Ensure Eureka server is accessible

3. **Kafka connection issues**
   - Wait for Zookeeper to fully start
   - Check Kafka UI at http://localhost:4002/
   - Verify Kafka is advertising correct listeners

4. **Database connection failures**
   - Ensure PostgreSQL/MongoDB containers are healthy
   - Check database credentials in config
   - Verify network connectivity between services

5. **Config server connection issues**
   - Verify config server can access [https://github.com/en-atul/banking-system-config](https://github.com/en-atul/banking-system-config)
   - Check config server logs for Git repository access errors
   - Ensure internet connectivity for remote config fetch
   - If using local config, verify file path in config server properties

## 🔄 Asynchronous Communication

### Kafka Integration
- **Kafka is required** for account creation and updates
- When an account is created/updated, notifications are sent asynchronously via Kafka
- **Kafka UI** at http://localhost:4002/ shows:
  - Topics: `account-notifications`
  - Message flow between Account Service and Notification Service
  - Consumer groups and offsets

### Notification Flow
1. **Account Service** creates/updates account
2. **Kafka Producer** sends notification event to `account-notifications` topic
3. **Notification Service** consumes the event asynchronously
4. **Notification** is saved to MongoDB
5. **User** can retrieve notifications via API

## 🔐 Testing Authentication

### 1. Register a user:
```bash
curl -X POST http://localhost:8765/auth/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### 2. Login to get access token:
```bash
curl -X POST http://localhost:8765/auth/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 1800
  }
}
```

### 3. Access protected endpoints with access token:
```bash
# Create account (requires USER or ADMIN role)
curl -X POST http://localhost:8765/account/api/v1/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "customerId": 1,
    "accountType": "SAVING",
    "balance": 10000
  }'

# Get account details
curl -X GET http://localhost:8765/account/api/v1/accounts/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Get notifications
curl -X GET http://localhost:8765/notification/api/v1/notifications \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 📋 API Endpoints

### Public Endpoints (No Authentication Required)
- `POST /auth/api/v1/users` - Register user
- `POST /auth/api/v1/auth/login` - Login
- `POST /auth/api/v1/auth/refresh` - Refresh token

### Protected Endpoints (Require Access Token)
- `GET/POST/PUT/DELETE /account/api/v1/accounts/**` - Account management
- `GET/POST/PUT/DELETE /customer/api/v1/customers/**` - Customer management
- `GET/POST/PUT/DELETE /transaction/api/v1/transactions/**` - Transaction management
- `GET/POST/PUT/DELETE /ledger/api/v1/ledgers/**` - Ledger management (ADMIN only)
- `GET/POST/PUT/DELETE /notification/api/v1/notifications/**` - Notification management

## 🔍 Monitoring and Debugging

### Service Discovery
- **Eureka Dashboard**: http://localhost:8761/
  - View all registered services
  - Check service health status
  - Monitor service instances

### Kafka Monitoring
- **Kafka UI**: http://localhost:4002/
  - Monitor topics and partitions
  - View message flow
  - Check consumer groups
  - Browse messages in topics

- **Kafdrop**: http://localhost:9000/
  - Alternative Kafka UI
  - Message browsing and inspection

### Health Checks
- **API Gateway**: http://localhost:8765/actuator/health
- **Auth Service**: http://localhost:8000/actuator/health
- **Account Service**: http://localhost:8001/actuator/health
- **Customer Service**: http://localhost:8002/actuator/health
- **Notification Service**: http://localhost:8500/actuator/health

## 🚨 Troubleshooting

### Common Issues
1. **"Access token is missing or invalid"**
   - Ensure you're using the API Gateway (port 8765)
   - Include `Authorization: Bearer YOUR_TOKEN` header
   - Check if token is expired

2. **"Service not found"**
   - Check Eureka dashboard at http://localhost:8761/
   - Ensure all services are running and registered

3. **Kafka connection issues**
   - Verify Kafka is running: `docker ps | grep kafka`
   - Check Kafka UI at http://localhost:4002/
   - Ensure notification service can connect to Kafka

4. **Database connection issues**
   - Check if PostgreSQL/MongoDB containers are running
   - Verify database credentials in application properties