# 🐾 Petshop API

A RESTful API for managing a petshop, built with **Java 21** and **Spring Boot 4**. It supports tutor registration, pet management, and appointment scheduling, with JWT-based authentication.

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 4**
- **Spring Security** (JWT authentication)
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL**
- **Lombok**
- **Bean Validation**
- **SpringDoc OpenAPI** (Swagger UI)
- **dotenv-java** (environment variable management)
- **Maven**

---

## 📋 Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL running locally (or any accessible instance)

---

## ⚙️ Configuration

Create a `.env` file in the project root with the following variables:

```env
DB_URL=jdbc:postgresql://localhost:5432/petshop_db
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_base64_encoded_secret
```

The application uses `ddl-auto: update`, so Hibernate will create/update the database schema automatically on startup.

---

## 🚀 Running the Application

```bash
# Clone the repository
git clone https://github.com/icaro-silva2108/api_petshop.git
cd api_petshop

# Build and run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 📖 API Documentation

Interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Authentication

This API uses **JWT Bearer tokens**. After signing in, include the token in your requests:

```
Authorization: Bearer <your_token>
```

---

## 📡 Endpoints

### Auth

| Method | Endpoint        | Description          | Auth Required |
|--------|-----------------|----------------------|---------------|
| POST   | `/auth/signin`  | Authenticate a tutor | No            |

### Tutors

| Method | Endpoint              | Description                    | Auth Required |
|--------|-----------------------|--------------------------------|---------------|
| POST   | `/tutors/signup`      | Register a new tutor           | No            |
| GET    | `/tutors/me`          | Get authenticated tutor info   | Yes           |
| PATCH  | `/tutors/{email}`     | Update tutor data              | Yes           |
| DELETE | `/tutors/{email}`     | Delete tutor account           | Yes           |
| GET    | `/tutors/{email}/pets`| List all pets of a tutor       | Yes           |

### Pets

| Method | Endpoint                          | Description        | Auth Required |
|--------|-----------------------------------|--------------------|---------------|
| POST   | `/pets`                           | Register a new pet | Yes           |
| PATCH  | `/pets/{tutor-email}/{id}`        | Update pet data    | Yes           |
| DELETE | `/pets/{tutor-email}/{id}`        | Remove a pet       | Yes           |

### Appointments

| Method | Endpoint                                         | Description                       | Auth Required |
|--------|--------------------------------------------------|-----------------------------------|---------------|
| POST   | `/appointments`                                  | Schedule a new appointment        | Yes           |
| GET    | `/appointments/pet/{pet-id}`                     | List appointments by pet          | Yes           |
| GET    | `/appointments/tutor/{tutor-id}`                 | List appointments by tutor        | Yes           |
| PATCH  | `/appointments/{tutor-id}/{appointment-id}/cancel`     | Cancel an appointment       | Yes           |
| PATCH  | `/appointments/{tutor-id}/{appointment-id}/reschedule` | Reschedule an appointment   | Yes           |
| PATCH  | `/appointments/{tutor-id}/{appointment-id}/complete`   | Mark appointment as complete| Yes           |

---

## 📦 Domain Enums

**Animal Types:** `DOG`, `CAT`, `BIRD`, `RABBIT`, `FISH`, `HORSE`

**Animal Sizes:** `SMALL`, `MEDIUM`, `LARGE`

**Animal Sex:** `MALE`, `FEMALE`

**Appointment Types:** `VETERINARY`, `JUST_GROOMING`, `JUST_BATH`, `COMPLETE_GROOMING`

**Appointment Status:** `SCHEDULED`, `RESCHEDULED`, `CANCELED`, `COMPLETED`

---

## 🏗️ Project Structure

```
src/main/java/com/icaro/api_petshop/
├── appointment/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   │   └── enums/
│   ├── repository/
│   └── service/
├── auth/
│   ├── controller/
│   ├── dto/
│   └── service/
├── config/
├── exceptions/
├── infra/
├── pet/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   │   └── enums/
│   ├── repository/
│   └── service/
└── tutor/
    ├── controller/
    ├── dto/
    ├── model/
    ├── repository/
    └── service/
```
