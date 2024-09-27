# Real-Time WebSocket and Kafka Integration

This project demonstrates a real-time communication system using WebSocket and Kafka. The WebSocket server consumes messages from a Kafka topic and broadcasts them to all connected WebSocket clients.

## Table of Contents

- [Real-Time WebSocket and Kafka Integration](#real-time-websocket-and-kafka-integration)
  - [Table of Contents](#table-of-contents)
  - [Architecture Overview](#architecture-overview)
  - [Prerequisites](#prerequisites)
  - [Setup and Installation](#setup-and-installation)
  - [Running the Project](#running-the-project)
    - [Testing the WebSocket](#testing-the-websocket)
  - [Kafka Configuration](#kafka-configuration)
  - [Project Structure](#project-structure)
  - [Endpoints](#endpoints)

## Architecture Overview

This project integrates **Kafka** and **WebSocket** to deliver real-time messages to clients. The general flow is as follows:

1. **Producers** send messages to a Kafka topic.
2. The **WebSocket server** subscribes to the Kafka topic as a consumer.
3. Messages received from Kafka are broadcasted to all connected WebSocket clients in real-time.

```plaintext
+------------+         +-----------------+        +----------------+       +-----------------+
|  Producers |  --->   |   Kafka Topic   | <---   | WebSocket Server| --->  | WebSocket Clients|
+------------+         +-----------------+        +----------------+       +-----------------+
   (e.g.,    |  Write  | (event stream)  |        |  Kafka Consumer|       |  (Browsers, Apps)|
 applications| messages|                 |        | WebSocket Handler      |                 |
 microservices)|       |                 |        | broadcasts to clients) |                 |
```

## Prerequisites

- **Java 17** or later
- **Apache Kafka** 2.8.0 or later
- **Maven** 3.6.0 or later
- **Spring Boot** 2.6.0 or later

## Setup and Installation

1. **Clone the repository**:

    ```bash
    [git clone https://github.com/your-repository-url.git](https://github.com/muhibbin-munna/websocket-kafka.git)
    cd websocket-kafka
    ```

2. **Set up Kafka**:
    - Download and extract [Apache Kafka](https://kafka.apache.org/downloads).
    - Start ZooKeeper:

      ```bash
      ./bin/zookeeper-server-start.sh config/zookeeper.properties
      ```

    - Start Kafka broker:

      ```bash
      ./bin/kafka-server-start.sh config/server.properties
      ```

3. **Build the project**:

    ```bash
    mvn clean install
    ```

4. **Run the application**:

    ```bash
    mvn spring-boot:run
    ```

## Running the Project

Once the project is running, you can interact with the WebSocket server via the `/ws` endpoint and publish messages to Kafka via the `/kafka/publish` endpoint.

### Testing the WebSocket

- **Web Browser**: Open a WebSocket client or web browser and connect to `ws://localhost:8080/ws`.
- **Postman**: Use the following steps to test the `/kafka/publish` endpoint:
  1. Open Postman.
  2. Send a POST request to `http://localhost:8080/kafka/publish` with a JSON body:

     ```json
     {
       "message": "Hello, WebSocket!"
     }
     ```

## Kafka Configuration

Kafka settings are managed in the `application.yml` file. The critical configuration properties include:

```yaml
kafka:
  consumer:
    bootstrap-servers: localhost:9092
    group-id: websocket-group
    auto-offset-reset: earliest
    key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
  listener:
    missing-topics-fatal: false
```

## Project Structure

- **KafkaConfig.java**: Configures the Kafka consumer factory and listener container factory, specifying how messages from Kafka topics should be consumed.

- **KafkaProducerService.java**: A service responsible for sending messages to a specified Kafka topic. It uses the `KafkaTemplate` to send the data.

- **WebSocketConfig.java**: Configures the WebSocket server. It sets up the endpoint `/ws` for WebSocket connections and allows cross-origin requests.

- **WebSocketController.java**: A WebSocket handler that listens to Kafka topics. When a message is received from Kafka, it is sent to all connected WebSocket clients.

- **KafkaController.java**: A REST controller that exposes an endpoint to publish messages to Kafka. It receives messages via HTTP POST requests and forwards them to Kafka.

## Endpoints

- **WebSocket Endpoint**:
  - `ws://localhost:8080/ws`: Connect to the WebSocket server.
  
- **Kafka REST Endpoint**:
  - `POST /kafka/publish`: Publish a message to Kafka.
    - **Request Body**:

      ```json
      {
        "message": "Your message here"
      }
      ```

    - **Response**:

      ```json
      {
        "status": "Message sent to Kafka topic websocket-topic"
      }
      ```
