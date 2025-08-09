# Kafka Notification System Setup

This document explains how to set up and test the asynchronous notification system using Kafka between the Account Service and Notification Service.

## 🚀 **Quick Start**

### 1. **Start Infrastructure Services**
```bash
docker-compose -f docker-compose.dev.yml up -d
```

This will start:
- PostgreSQL (port 5336)
- MongoDB (port 27017)
- Zookeeper (port 2181)
- Kafka (port 9092)

### 2. **Start Microservices**
```bash
# Start Account Service
cd services/account-service
mvn spring-boot:run

# Start Notification Service (in another terminal)
cd services/notification-service
mvn spring-boot:run
```

## 📋 **How It Works**

### **Account Service (Producer)**
- When an account is created → sends `ACCOUNT_CREATED` event to Kafka
- When an account is updated → sends `ACCOUNT_UPDATED` event to Kafka
- Events are sent asynchronously (non-blocking)

### **Notification Service (Consumer)**
- Listens to Kafka topic `account-notifications`
- Processes incoming events
- Stores notifications in MongoDB
- Provides REST API to retrieve notifications

## 🔧 **Configuration**

### **Kafka Topic**
- **Topic Name**: `account-notifications`
- **Partitions**: Default (1)
- **Replication Factor**: Default (1)

### **Kafka Configuration**
- **Bootstrap Servers**: `localhost:9092`
- **Producer**: Account Service
- **Consumer Group**: `notification-service-group`
- **Consumer**: Notification Service

## 📡 **API Endpoints**

### **Account Service**
- `POST /api/v1/accounts` - Create account (triggers notification)
- `PUT /api/v1/accounts/{id}` - Update account (triggers notification)

### **Notification Service**
- `GET /api/v1/notifications/user/{userId}` - Get all notifications for user
- `GET /api/v1/notifications/user/{userId}/unread` - Get unread notifications
- `GET /api/v1/notifications/user/{userId}/count/unread` - Get unread count
- `PUT /api/v1/notifications/{id}/read` - Mark notification as read

## 🧪 **Testing the System**

### **1. Create an Account**
```bash
curl -X POST http://localhost:8100/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "SAVINGS",
    "balance": 1000.00
  }'
```

### **2. Check Notifications**
```bash
# Get notifications for user 1
curl http://localhost:8500/api/v1/notifications/user/1

# Get unread notifications
curl http://localhost:8500/api/v1/notifications/user/1/unread
```

### **3. Update an Account**
```bash
curl -X PUT http://localhost:8100/api/v1/accounts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "SAVINGS",
    "balance": 1500.00
  }'
```

## 📊 **Event Structure**

### **NotificationEvent DTO**
```json
{
  "eventType": "ACCOUNT_CREATED",
  "userId": 1,
  "accountId": 1,
  "accountNumber": "123456789012",
  "accountType": "SAVINGS",
  "balance": 1000.00,
  "timestamp": "2024-01-01T10:00:00",
  "message": "Your account has been created successfully!"
}
```

## 🔍 **Monitoring**

### **Kafka Topics**
```bash
# List topics
docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# Describe topic
docker exec -it kafka kafka-topics.sh --describe --topic account-notifications --bootstrap-server localhost:9092
```

### **Kafka Consumer Groups**
```bash
# List consumer groups
docker exec -it kafka kafka-consumer-groups.sh --list --bootstrap-server localhost:9092

# Describe consumer group
docker exec -it kafka kafka-consumer-groups.sh --describe --group notification-service-group --bootstrap-server localhost:9092
```

## 🚨 **Troubleshooting**

### **Common Issues**
1. **Kafka not starting**: Check if Zookeeper is running
2. **Connection refused**: Verify ports are not blocked
3. **Topic not found**: Check if auto-create is enabled
4. **Consumer not receiving**: Verify consumer group configuration

### **Logs**
```bash
# Account Service logs
docker logs account-service

# Notification Service logs
docker logs notification-service

# Kafka logs
docker logs kafka
```

## 🎯 **Benefits of This Architecture**

- **Asynchronous**: Account operations don't wait for notifications
- **Scalable**: Multiple notification consumers can be added
- **Reliable**: Kafka provides message persistence and delivery guarantees
- **Decoupled**: Services communicate via events, not direct calls
- **Fault Tolerant**: If notification service is down, messages are queued 