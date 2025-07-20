# Banking System Microservices with JWT Authentication

This project implements a microservices banking system with centralized JWT authentication at the API Gateway level.

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

## Building and Running

### Prerequisites
- Java 17+
- Maven 3.6+

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

### Running Services
1. Start Config Server
2. Start Naming Server (Eureka)
3. Start API Gateway
4. Start Auth Service
5. Start other services as needed

### Testing Authentication

#### 1. Register a user:
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

#### 2. Login to get token:
```bash
curl -X POST http://localhost:8765/auth/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### 3. Access protected endpoint:
```bash
curl -X GET http://localhost:8765/account/123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```