# NWTiS

 Advanced Web Technologies and Services Project
 ---
 Developed as part of the 2023/2024 academic curriculum for the Advanced Web Technologies and Services course.

# Telematics System for Electric Vehicle Monitoring and Analysis

## Project Overview

The **Telematics System for Electric Vehicle Monitoring and Analysis** is a comprehensive system designed to monitor and analyze electric vehicle (EV) driving behavior and performance metrics. The system processes real-time data, facilitates communication between various server components, and stores information in a relational database using JPA's Criteria API for dynamic querying and data retrieval.

### Key Features

- **Data Collection and Monitoring**: Collects and monitors various data points related to EV operations, such as speed, battery usage, and driving patterns.
- **Database Storage**: All data is stored in a relational database, managed via JPA (Java Persistence API), and retrieved dynamically using the Criteria API for flexible queries.
- **Server Communication**: Multiple server components (e.g., `CentralSystem`, `RadarServer`, and `KazneServer`) communicate via network sockets to process telematics data by sending and receiving commands and responses.
- **Data Validation**: Regular expressions ensure that incoming data is validated, maintaining proper formatting and integrity before processing.
- **Scalable and Reliable**: Built for scalability to handle large datasets and future expansion.

### Technology Stack

- **Java**: The primary programming language for developing the server components and database interaction.
- **JPA & Criteria API**: Manages database interactions with dynamic and flexible querying, providing type-safe retrieval of data.
- **Maven**: Dependency management and project building.
- **JUnit**: Unit testing framework to ensure the correctness of individual components.
- **Network Sockets**: Communication between server components.

### Key Components

1. **CentralSystem**:
   - The central server responsible for coordinating the entire system, processing incoming commands, and analyzing telematics data.

2. **RadarServer**:
   - Processes radar data, such as environmental information about nearby vehicles or obstacles, and stores relevant data in the database.

3. **KazneServer**:
   - Handles detailed vehicle metrics like acceleration, braking, and speed, storing this fine-grained data in the database for analysis.

4. **Database Interaction (JPA & Criteria API)**:
   - **Entities**: Data is stored in the database as entities (e.g., `VehicleData`, `RadarData`, `DrivingBehavior`), which directly map to database tables.
   - **Criteria API**: Provides a type-safe, dynamic query mechanism for flexible and efficient data retrieval. This ensures safe and accurate querying without exposing SQL errors.

### System Workflow

1. **Data Collection**: Each server component collects data from different vehicle sensors (e.g., speed, radar data, driving behavior).
   
2. **Data Storage**: The data is then stored in a relational database using JPA. Entities are used to map the data to the appropriate database tables, ensuring efficient storage and retrieval.

3. **Querying Data**: The Criteria API allows dynamic querying based on various parameters such as vehicle ID, time intervals, and driving behavior metrics, making data retrieval flexible and type-safe.

4. **Communication Between Servers**: The system relies on network sockets for communication between the `CentralSystem`, `RadarServer`, and `FineServer`. These servers exchange commands and data to ensure the system functions cohesively.

### Database Interaction

- **JPA**: The system uses JPA to map entities to database tables. Each data type (e.g., `VehicleData`, `RadarData`) is represented as an entity that corresponds to a table in the database.
- **Criteria API**: The Criteria API allows for type-safe and dynamic querying of the database. Instead of writing raw SQL queries, the Criteria API enables the building of queries using Java objects, reducing the risk of SQL injection and syntax errors.


## Architecture of the system and methods of RESTful web services
![image](https://github.com/user-attachments/assets/9ee022ed-92cb-4ecc-8165-e9c714e3b52e)


