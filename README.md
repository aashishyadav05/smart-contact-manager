# Smart Contact Manager

A secure contact management system built with Spring Boot and Thymeleaf. Users can sign up, verify their email, and manage their contacts in a personalized dashboard.

## 💻 Tech Stack

- Java 17
- Spring Boot 3.x
- Thymeleaf
- Spring Security
- **PostgreSQL** (previously MySQL)
- Maven

## ✅ Features

- User signup/login with email verification
- Role-based access (USER/ADMIN)
- Add, edit, delete, search contacts
- Contact image upload and preview
- Secure backend with Spring Security
- Pagination for efficient browsing

## 📁 Project Structure


```
smartcontactmanager/
├── src/main/java/...
├── src/main/resources/
├── target/ (ignored)
├── pom.xml
└── README.md
```

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/aashishyadav05/smart-contact-manager.git
   cd smart-contact-manager

   ```
2. Setup database in `application.properties`
   Example PostgreSQL config:
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/smartcontactdb
   spring.datasource.username=your_postgres_username
   spring.datasource.password=your_postgres_password
   
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   ```
   
4. Run the app using Eclipse or:
   ```bash
   mvn spring-boot:run
   ```
