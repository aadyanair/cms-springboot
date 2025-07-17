# Complaint Management System 🛠️

A Spring Boot-based platform for students to raise hostel complaints and for admins/wardens to manage and resolve them.

## ✅ Features Implemented So Far

- User Registration & Login with JWT Authentication
- MySQL Integration
- Secure Complaint Submission linked to the logged-in user
- View all personal complaints (for students)

## 🏗️ Tech Stack

| Layer      | Tech                       |
|------------|----------------------------|
| Backend    | Spring Boot, Spring Data JPA |
| Auth       | Spring Security + JWT      |
| Database   | MySQL                      |
| Dev Tools  | Lombok, Spring Boot DevTools |

## 🔐 JWT Flow

- User logs in → receives token  
- Token is sent in `Authorization: Bearer <token>` header  
- Secured endpoints validate the token using filters