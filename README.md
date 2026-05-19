# 🐾 Pet Shop API

A RESTful API for managing tutors, pets, and service appointments in a pet shop.

This project was built to practice modern backend development with Java and Spring Boot, covering authentication, authorization, data validation, business rules, and soft delete.

---

## 🚀 Features

### 👤 Tutor Management
- Sign up new tutors
- Authenticate with JWT
- Update profile information
- Soft delete tutor accounts
- View authenticated tutor profile (`/tutors/me`)
- List all active pets owned by the tutor

### 🐶 Pet Management
- Register pets linked to the authenticated tutor
- Update pet information
- Soft delete pets
- Ownership validation (a tutor can only manage their own pets)

### 📅 Appointment Management
- Schedule appointments for services
- Cancel appointments
- Reschedule appointments
- Mark appointments as completed
- List appointments by tutor or pet

### 🔐 Security
- JWT-based authentication
- Protected endpoints using Spring Security
- Ownership checks for all sensitive operations

### 📄 API Documentation
- Interactive Swagger/OpenAPI documentation

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- Hibernate
- PostgreSQL
- Bean Validation
- Lombok
- Swagger / OpenAPI
- Maven

---

## 🧱 Project Architecture

The application follows a layered architecture:

```text
Controller → Service → Repository → Database
```

### Layers
- **Controller**: Handles HTTP requests and responses.
- **Service**: Contains business rules and validations.
- **Repository**: Data access with Spring Data JPA.
- **DTOs**: Separate request and response models.

---

## 📂 Project Structure

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

---

## 🗄️ Database Model

### Tutor
- id
- name
- email
- passwordHash
- active

### Pet
- id
- tutor
- name
- type
- sex
- breed
- size
- age
- active

### Appointment
- id
- petTutor
- pet
- serviceType
- status
- scheduledDateTime
- createdAt
---
# 🐋 Docker Support
This project includes full Docker support for both the Spring Boot API and the PostgreSQL database.

### Docker Files

#### Dockerfile

- Defines the image used to build and run the application.

```dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/api_petshop-0.0.1-SNAPSHOT.jar"]
```

#### docker-compose.yml
- Orchestrates the API and PostgreSQL containers.

```yaml
version: "3.9"

services:
  postgres:
    image: postgres:17
    container_name: petshop-db
    environment:
      POSTGRES_DB: petshop
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    container_name: petshop-api
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/petshop
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      JWT_SECRET: your-base64-secret
    ports:
      - "8080:8080"

volumes:
  postgres_data:
```
---

## 🔄 Appointment Status Flow

- `SCHEDULED`
- `RESCHEDULED`
- `CANCELED`
- `COMPLETED`

Business rules prevent invalid transitions, such as:
- Canceling an already completed appointment
- Completing a canceled appointment
- Scheduling or rescheduling in the past

---

## 🧹 Soft Delete

Instead of physically removing data, tutors and pets are marked as inactive using an `active` flag.

### Benefits
- Preserves appointment history
- Avoids foreign key constraint issues
- Prevents access to deleted records

---

## 🔐 Authentication Flow

1. Tutor signs up.
2. Tutor logs in with email and password.
3. API returns a JWT token.
4. Token is used as a Bearer Token in protected endpoints.

### Example Login Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tutorResponse": {
    "id": 1,
    "name": "Icaro Silva",
    "email": "icarosilva@email.com"
  }
}
```

---

## 📌 Main Endpoints

### Auth
| Method | Endpoint | Description |
|------|------|------|
| POST | `/auth/signin` | Authenticate tutor |

### Tutors
| Method | Endpoint | Description |
|------|------|------|
| POST | `/tutors/signup` | Create account |
| GET | `/tutors/me` | Get authenticated tutor |
| PATCH | `/tutors/me` | Update profile |
| DELETE | `/tutors/me` | Soft delete account |
| GET | `/tutors/me/pets` | List active pets |

### Pets
| Method | Endpoint | Description |
|------|------|------|
| POST | `/pets` | Register pet |
| PATCH | `/pets/{id}` | Update pet |
| DELETE | `/pets/{id}` | Soft delete pet |

### Appointments
| Method | Endpoint | Description |
|------|------|------|
| POST | `/appointments` | Schedule appointment |
| PATCH | `/appointments/{id}/cancel` | Cancel appointment |
| PATCH | `/appointments/{id}/reschedule` | Reschedule appointment |
| PATCH | `/appointments/{id}/complete` | Complete appointment |
| GET | `/appointments/tutor` | List tutor appointments |
| GET | `/appointments/pet/{petId}` | List appointments by pet |

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/icaro-silva2108/api_petshop.git
cd api_petshop
```

### 2. Configure PostgreSQL

Create a database:

```sql
CREATE DATABASE api_petshop;
```

### 3. Configure Environment Variables

Create a `.env` file or set the following variables:

```env
DB_URL=jdbc:postgresql://localhost:5432/api_petshop
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

### 5. Access Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

---
# 📦 Running Locally with Docker
To build and start the complete application stack (API + PostgreSQL), run:
```bash
docker compose up --build
```

After the containers are started, the application will be available at:

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html  

To stop the cointainers:
```bash
docker compose down
```

---
## 🧪 Testing the API

You can test the endpoints using:
- Postman
- Swagger UI
- Insomnia

Typical workflow:
1. Create a tutor account.
2. Sign in to receive a JWT token.
3. Authorize requests with Bearer Token.
4. Create pets.
5. Schedule appointments.

---

## 📚 Key Concepts Practiced

- REST API design
- Layered architecture
- JWT authentication and authorization
- DTO pattern
- Bean Validation
- Exception handling
- Soft delete
- Ownership validation
- JPA entity relationships
- Transaction management with `@Transactional`

---

## 🔮 Future Improvements

- Unit and integration tests
- CI/CD pipeline
- Automatic auditing

---

## 👨‍💻 Author

**Icaro Pelanda Silva**

- LinkedIn: https://www.linkedin.com/in/icaro-silva-10885a365/
- GitHub: https://github.com/icaro-silva2108

---

## 📄 License

This project is for educational purposes.
