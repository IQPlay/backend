# Backend

This is the backend for **IQPlay**, a web project focused on memory training. It provides the necessary server-side logic and data management for the game’s functionalities.

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE.txt)
[![Build & test](https://github.com/IQPlay/backend/actions/workflows/gradle-build-test.yml/badge.svg?branch=main)](https://github.com/IQPlay/backend/actions/workflows/gradle-build-test.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
<br/>
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=IQPlay_backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=IQPlay_backend)
[![codecov](https://codecov.io/github/IQPlay/backend/graph/badge.svg?token=CHZQR529XU)](https://codecov.io/github/IQPlay/backend)

## Getting Started

### Prerequisites

Ensure that the following are installed on your system:

- **Java 21** or higher: [Download JDK](https://www.oracle.com/fr/java/technologies/downloads/)
- **Gradle 8.10.2** or higher (optional if using the provided Gradle Wrapper): [Download Gradle](https://gradle.org/)

### Setup Instructions

1. **Clone the Repository**  
   Clone this repository into a directory of your choice:
   ```bash
   git clone https://github.com/IQPlay/backend.git
   ```

2. **Navigate to the Repository**  
   Open a terminal in the cloned directory:
   ```bash
   cd backend
   ```

3. **Run the Application**  
   Use the Gradle Wrapper to launch the Spring Boot server:
   ```bash
   ./gradlew run
   ```

4. **Access the Application**  
   Open a web browser and go to the following address to see the result:
   ```plaintext
   http://localhost:8080/hello
   ```
