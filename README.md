 Architecture
The system is built using 3 independent microservices:

1. Booking Service –  Handles booking requests
2. Seat Reservation Service – Manages movie catalog & seat availability
3. Payment Service – Processes payments
4. Movie Ticket Booking - commons service

🔹 Design Pattern Used
- SAGA Pattern (Choreography-based)
- Event-driven communication between services
- Compensation transactions for failure handling

🔹 Tech Stack
- Java 17+
- Spring Boot 3.x
- Kafka / Event Broker
- REST APIs
- Maven
- MySQL
