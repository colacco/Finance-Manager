# Finance-Manager
Back-end REST API Java (Spring), **created as a study and practice project**. 
Finance Manager — a web application to practice back-end concepts, REST APIs and database integration.

![Rafael Account](https://github.com/colacco/Finance-Manager/blob/main/images/userRafael.png)

## 📋 About Project
This project was developed as a study application to practice concepts about back-end concepts such as REST APIs, PostgreSQL integration, database migrations and HTTP methods. The Front-end was generated with AI to provide interaction with te API wich allowed me to folly focus on learning and developing the backwnd. 

## 🛠️ Technologies Used

### Back-end
- Java 21
- Spring 3.5.5
- Spring dependencies: Spring Data JPA, PostgreSQL Driver, Flyway Migration, Validation, Lombok and Spring Web
- PostgreSQL

### Front-end
- HTML5
- CSS3
- JavaScript

## 💡 Features
- User Autentication
- Transaction management (full CRUD)
- Transaction types (INPUT, OUTPUT)
- Real-time balance calculation
- Balance validartion (prevents negative balance where applicable)
- User transaction history

## 🏗️ Archtecture
Main structure(```src/main/java/com/colacco/finance```)

![archtecture](https://github.com/colacco/Finance-Manager/blob/main/images/archtecture.png)

## API Endpoints

User (/user):
```
  GET      /user/list        # List users
  POST     /register         # Register a new user
  PUT      /update           # Update user
  DELETE   /delete           # Delete user
```

![GET user list](https://github.com/colacco/Finance-Manager/blob/main/images/getUsers.png)
Transaction (/{usuarioId}):
```
  GET      /list             # List transactions (paged)
  GET      /total            # Sum all transactions
  POST     /{io}/launch      # Create a new transaction (io: 1=input, 2=output)
  PUT                        # Update transaction
  DELETE   /{transactionId}  # Delete transaction
```
![PUT transactions](https://github.com/colacco/Finance-Manager/blob/main/images/updateTransaction.png)

## ✅ Project strengths
- Input validation
- Complete CRUD for transactions
- Data integrity control
- Basic user system

## ⚠️ Identified Improvement Points
### Security:
   - Unencrypted passwords
   - Absence of JWT authentication
   - No 2FA
### Performance:
   - balance() method with O(n)
   - No cache
   - Non-optimized queries

 ## 🎯 Achieved Objectives:
   - Create API REST
   - PostgreSQL integration
   - Full CRUD for transactions
   - Basic validations implemented

## 📈 Next Steps:
  - Implement Spring Security (authentication and authorization)
  - Hash passwords with BCrypt
  - Implement JWT for stateless authentication
  - Optimize queries and reduce balance() complexity 
  - Add unit and integration tests

## ▶️ How to run
### Prerequisites:
 - Java 21+
 - PostgreSQL
 - Maven

### Configuration

1. Clone the repository
2. Configure ```application.properties``` (or environment variables):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```
3. Run migrations (Flyway)
4. Start the application with Maven:
```
mvn spring-boot:run
```

## 🤖 Note about the Frontend:
The frontend was developed entirely with AI, allowing full focus on the backend. This approach enabled:
- Focused on backend skills
- Provided a functional interface for testing the API
- More time for API refinement

![Register user](https://github.com/colacco/Finance-Manager/blob/main/images/userRegister.png)
