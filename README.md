# ShopApp Backend

A robust e-commerce backend application built with Spring Boot.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Setting Up](#setting-up)
- [Security](#security)

## Overview

ShopApp is a comprehensive e-commerce backend solution that provides RESTful APIs for managing products, orders, categories, and users. It includes authentication, authorization, and comprehensive error handling.

## Features

- User authentication and registration with JWT
- Role-based authorization (USER, ADMIN)
- Product management with image uploads
- Order processing
- Category management
- Detailed order tracking

## Tech Stack

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Security** for authentication and authorization
- **Spring Data JPA** for database access
- **MySQL** as the database
- **JWT** (JSON Web Token) for stateless authentication
- **ModelMapper** for DTO-Entity conversion
- **Lombok** for reducing boilerplate code
- **Maven** for dependency management

## Project Structure

The application follows a standard layered architecture:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Interfaces with the database
- **Model Layer**: Defines entity classes
- **DTO Layer**: Data Transfer Objects for API requests/responses
- **Exception Layer**: Custom exceptions for error handling
- **Configuration**: Security and application configurations

## Database Schema

The database consists of the following main tables:

- `users`: Stores user information and credentials
- `roles`: Defines user roles (ADMIN, USER)
- `products`: Contains product details
- `product_images`: Stores product images (up to 5 per product)
- `categories`: Defines product categories
- `orders`: Contains order information
- `order_details`: Links products to orders with quantities and prices
- `tokens`: Manages JWT tokens

## API Endpoints

### Authentication

- `POST /api/v1/users/register`: Register a new user
- `POST /api/v1/users/login`: Authenticate and get JWT token

### Products

- `GET /api/v1/products`: Get paginated list of products
- `GET /api/v1/products/{id}`: Get a specific product
- `POST /api/v1/products`: Create a new product (ADMIN)
- `PUT /api/v1/products/{id}`: Update a product (ADMIN)
- `DELETE /api/v1/products/{id}`: Delete a product (ADMIN)
- `POST /api/v1/products/uploads/{id}`: Upload product images (ADMIN)

### Categories

- `GET /api/v1/categories`: Get all categories
- `POST /api/v1/categories`: Create a new category (ADMIN)
- `PUT /api/v1/categories/{id}`: Update a category (ADMIN)
- `DELETE /api/v1/categories/{id}`: Delete a category (ADMIN)

### Orders

- `POST /api/v1/orders`: Create a new order (USER)
- `GET /api/v1/orders/{id}`: Get a specific order
- `GET /api/v1/orders/user/{user_id}`: Get all orders for a user
- `PUT /api/v1/orders/{id}`: Update an order (ADMIN)
- `DELETE /api/v1/orders/{id}`: Delete an order (ADMIN)

### Order Details

- `POST /api/v1/order_details`: Add a product to an order (USER)
- `GET /api/v1/order_details/{id}`: Get specific order details
- `GET /api/v1/order_details/order/{orderId}`: Get all details for an order
- `PUT /api/v1/order_details/{id}`: Update order details (ADMIN)
- `DELETE /api/v1/order_details/{id}`: Delete order details (ADMIN)

## Setting Up

### Prerequisites

- Java 17 or higher
- MySQL Server
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd shopapp-backend
   ```

2. Create MySQL database:
   ```sql
   CREATE DATABASE shopapp;
   ```

3. Run the database script:
   ```bash
   mysql -u root -p shopapp < database.sql
   ```

4. Configure application properties:
   
   Edit `src/main/resources/application.yml` file to match your MySQL configuration.

5. Build the application:
   ```bash
   mvn clean install
   ```

6. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start at `http://localhost:8088`

## Security

The application uses JWT (JSON Web Token) for authentication:

- Tokens are generated when users login
- Tokens expire after 30 days (configurable)
- Protected routes require a valid JWT in the Authorization header
- Role-based access control for different API endpoints

Example authentication header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---
