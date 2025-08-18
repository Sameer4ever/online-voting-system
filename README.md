# 🗳️ Online Voting System

[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Swagger](https://img.shields.io/badge/Documentation-Swagger-85EA2D)](http://localhost:8080/swagger-ui.html)

A secure digital voting platform with **full API documentation** via Swagger UI. React frontend coming soon!

---

## 🚀 Features

| Feature                | Description                                                                |
|------------------------|----------------------------------------------------------------------------|
| 🔐 **Secure Auth**     | JWT token-based authentication with role validation                        |
| 👥 **Role Management** | Voter, Candidate & Admin roles with granular permissions                   |
| 🗳️ **One-Vote Policy** | Cryptographic prevention of duplicate voting                               |
| 📊 **Real-time Stats** | Live candidate vote tracking with MongoDB aggregations                     |
| 📱 **Responsive API**  | Ready for web/mobile frontends (React coming Q1 2025)                      |

---

## 🛠️ Tech Stack

**Backend Core**:
- Java 17 (LTS)
- Spring Boot 3.1.5
- Spring Security + JWT
- MongoDB Atlas

**Developer Tools**:
- Swagger UI (Interactive API Docs)
- Lombok
- Maven

---

## 📦 Installation

```bash
# 1. Clone repository
git clone https://github.com/Sameer4ever/online-voting-system.git
cd online-voting-system

# 2. Run backend (requires Java 17+)
mvn spring-boot:run
