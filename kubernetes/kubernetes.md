# Kubernetes Setup for Hexagonal Orders Microservice

## Overview
This project demonstrates how to run a Spring Boot microservice (Hexagonal Orders) and a Redpanda (Kafka-compatible) stack in Kubernetes using Minikube. The setup includes:
- A scalable microservice deployment (2 replicas)
- Redpanda broker for event-driven messaging
- Redpanda Console for Kafka topic management
- Kubernetes Services for internal and external access

---

## Architecture Diagram

```
+-------------------+         +-------------------+
|                   |         |                   |
|  hexagonalorders  | <-----> |    Redpanda       |
|  Deployment (2x)  |   |     |  Deployment (1x)  |
|                   |   |     |                   |
+-------------------+   |     +-------------------+
         |              |             ^
         |              |             |
         v              |     +-------------------+
+-------------------+   |     |                   |
|                   |   +---->| Redpanda Console  |
|  Service (NodePort)|         |  Deployment (1x)  |
+-------------------+         +-------------------+
```

---

## What Was Deployed

### 1. **Spring Boot Microservice (hexagonalorders)**
- **Deployment:** 2 replicas for high availability
- **Service:** NodePort (port 30080) for external access
- **Environment:** Connects to Redpanda at `redpanda:9092`

### 2. **Redpanda Broker**
- **Deployment:** 1 replica
- **Service:** ClusterIP (internal access on ports 9092, 9644, 8082)
- **Purpose:** Kafka-compatible event streaming

### 3. **Redpanda Console**
- **Deployment:** 1 replica
- **Service:** NodePort (port 30081) for external access
- **Purpose:** Web UI for managing Kafka topics and messages

---

## How to Use

### 1. **Build the Microservice Docker Image**
```sh
mvn clean package
# Use Minikube's Docker daemon
eval $(minikube docker-env)
docker build -t hexagonalorders:latest .
```

### 2. **Deploy to Kubernetes**
```sh
kubectl apply -f kubernetes/
```

### 3. **Access the Services**
- **Microservice:**
  ```sh
  minikube service hexagonalorders
  ```
- **Redpanda Console:**
  ```sh
  minikube service redpanda-console
  ```

### 4. **Check Pod Status**
```sh
kubectl get pods
```

---

## Troubleshooting
- **Image Pull Issues:**
  - The deployment uses `imagePullPolicy: Never` to use the locally built image in Minikube.
- **Redpanda Pod CrashLoopBackOff:**
  - Ensure no unsupported command-line arguments are passed. The deployment uses the default Redpanda entrypoint.
- **Service Not Accessible:**
  - Make sure Minikube is running and the pods are in `Running` state.

---

## Customization
- You can scale the microservice by editing `hexagonalorders-deployment.yaml` and changing the `replicas` field.
- Update environment variables as needed for different Kafka brokers or profiles.

---

## Summary
This setup provides a robust local development environment for event-driven microservices using Kubernetes, Redpanda, and Spring Boot. It is easily extensible for more services or production-grade deployments. 