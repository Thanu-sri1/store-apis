# Store Microservices Refactor

This repository contains a refactored version of the monolithic Store API, now split into a modern microservices architecture.

## 🚀 Overview

The system is built with **Spring Boot 3.4**, **Spring Cloud Gateway**, and **Docker**. It decomposes the original monolith into domain-driven services while maintaining a secure, centralized authentication model.

### Services (Backend)
- **[Store Gateway](./backend/store-gateway)**: API Gateway (Port 8080)
- **[Auth Service](./backend/store-auth-service)**: Authentication & JWT (Port 8081)
- **[User Service](./backend/store-user-service)**: Account & Profile (Port 8082)
- **[Product Service](./backend/store-product-service)**: Catalog & Inventory (Port 8083)
- **[Cart Service](./backend/store-cart-service)**: Shopping Cart (Port 8084)
- **[Order Service](./backend/store-order-service)**: Checkout & Stripe (Port 8085)

## 🏗️ Architecture

For a detailed look at the system design and DevOps workflow, see the **[Architecture Blueprint](./backend/architecture_blueprint.md)**.

## 📂 Project Structure
- **[/frontend](./frontend)**: React + Redux Frontend
- **[/backend](./backend)**: Spring Boot Microservices

## 🛠️ Quick Start

### 1. Requirements
- Docker & Docker Compose
- Maven 3.8+ (for local BE runs)
- Node.js 18+ (for local FE runs)

### 2. Launching with Docker
Run the entire stack (including MySQL) with a single command from the root:
```bash
docker-compose up --build
```

### 3. Environment Setup
Create a `.env` file in the root directory with the following:
```env
JWT_SECRET=your_32_char_secret_key
STRIPE_KEY=your_stripe_secret_key
WEBHOOK_SECRET_KEY=your_stripe_webhook_secret
```

## 🔐 Security Model
Authentication is handled at the **Gateway** level. Valid JWTs result in identity propagation via `X-User-Id` and `X-User-Role` headers to all downstream microservices.

---
*Refactored by Antigravity AI*
