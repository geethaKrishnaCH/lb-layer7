# Load Balancer Project

This project is a custom HTTP Load Balancer that distributes incoming client requests across multiple backend servers. It supports two load balancing algorithms, health checks, dynamic server management, and algorithm switching at runtime.

## Features

- **Load Balancing Algorithms**: Supports switching between different strategies (e.g., round-robin, random).
- **Dynamic Server Management**: Add or remove backend servers dynamically via API endpoints.
- **Health Checks**: Periodic health checks on servers, with automatic monitoring and recovery.
- **Configurable Setup**: Customizable server list and load balancing algorithm via JSON configuration.

## Project Structure

```plaintext
├── lb
│   ├── config
│   ├── handlers
│   ├── health
│   ├── strategy
│   |── LBApp.java
|   |── LoadBalanacer.java
|   |── LoadBalancerServer.java
Resources
├── resources
│   |── config.json
|   |── log4j2.xml
└── README.md
```

## Getting Started

### Prerequisites

- **Java 17**: Required for running the project.
- **Maven**
- **JSON Configuration File**: A `config.json` file to define initial server URLs and load balancing algorithm.

## Installation

### Clone the Repository

```bash
git clone https://github.com/geethaKrishnaCH/lb-layer7.git
cd lb-layer7
```

## Configure Servers and Algorithm

Update `config.json` with the desired server URLs and load balancing algorithm (e.g., round-robin, random).

```json
{
  "servers": [
    { "url": "http://localhost:8081", "name": "Server 1" },
    { "url": "http://localhost:8082" }
  ],
  "algorithm": "round-robin"
}
```

## Build the Project

Compile and Run the project using maven:

```bash
mvn clean package
java -jar target/*.jar
```

## API Endpoints

- **`/`**: Forwards a request to one of the backend servers.
- **POST `/add`**: Adds a new server. Expects `{ "url": "server-url" }` in the request body.
- **POST `/remove`**: Removes a server. Expects `{ "url": "server-url" }` in the request body.
- **PUT `/changeAlgorithm`**: Changes the load balancing algorithm. Expects `{ "algorithm": "algorithm-name" }`.
- **GET `/status`**: Lists the current healthy and unhealthy servers and the active algorithm.

## Health Checks

The load balancer performs automatic health checks to monitor server availability:

- **Healthy Server Monitoring**: Checks all active servers at regular intervals (every 10s).
- **Unhealthy Server Monitoring**: Rechecks unhealthy servers to detect if they’ve come back online (every Minute).
