# Server README

Welcome to the Server repository for the Client App! This server, built using the Ktor framework, is designed to communicate with a basic Android application. It ensures reliable, low-latency communication and can handle disconnections and reconnections effectively, maintaining a high fidelity of data exchange.

## Features

- Sends messages to the connected Android app every 2 seconds.
- Uses WebSocket protocol for reliable, low-latency communication.
- Handles connections, disconnections, and reconnections without data loss or delay beyond a 5-second threshold.

## Installation

### Prerequisites

- Kotlin and Ktor installed on your development machine.
- Gradle build tool.
- Intel J idea or open on android studio

### Steps

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/somtorizm/com.ezinwavictor.ktor-sample-server.git
    ```

2. **Install Dependencies and Build Gradle**:
    ```bash
    ./gradlew install
    ```

3. **Run the Server**:
    ```bash
    ./gradlew run
    ```

The server will start and begin sending messages to the connected Android app every 2 seconds.

## Usage

1. **Run the Server**:
    - Ensure the server is running. It will send a message every 2 seconds to any connected Android app.

2. **Connect the Android App**:
    - The Android app will connect to the server using WebSocket and start receiving messages.

## Project Structure

- **src/main/kotlin**: Contains the main server code.
- **build.gradle.kts**: Contains the build configuration.

## Notes

- Ensure that your Android app is configured to connect to the correct server address and port.
- The server handles message delivery reliably, ensuring no data is lost during disconnections.

---
