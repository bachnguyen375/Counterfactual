# CD490 Code Project

## Contributor: Bach Nguyen, Bikki Panthi

### This is the HTTP server component of **Project 1: Counterfactual**.
### The server is written in Java and provides a basic local HTTP server for serving web resources. 
### The server-side implementation is based on the [CodeFromScratch HTTP Server tutorial](https://github.com/CoderFromScratch/simple-java-http-server).
---

## Features

* Java-based HTTP server
* Configurable server port
* Configurable web root
* JSON-based configuration
* Socket-based HTTP connection handling
* Multithreaded server listener
* SLF4J logging
* Localhost development environment

---
### Components

| Class                  | Description                                             |
| ---------------------- | ------------------------------------------------------- |
| `HTTPServer`           | Main entry point that initializes and starts the server |
| `ConfigurationManager` | Loads and manages the server configuration              |
| `Configuration`        | Stores configuration values such as port and web root   |
| `ServerListenerThread` | Listens for incoming HTTP connections                   |
| `http.json`            | Contains the server's runtime configuration             |

---

## Configuration

The server configuration is stored in:

```text
src/main/resources/http.json
```

The configuration specifies the server's port and web root.
For example:

```json
{
  "port": 8080,
  "webroot": "public"
}
```

### `port`

The TCP port used by the HTTP server.
For example:
```text
8080
```
The server can then be accessed at:
```text
http://localhost:8080
```

### `webroot`
The directory used as the root location for resources served by the HTTP server.

---
## Running the Server
### 1. Open `HTTPServer`
Locate:
```text
src/main/java/com/counterfactual/httpserver/HTTPServer.java
```
### 2. Run `main()`
Run the `main()` method:
```java
public static void main(String[] args)
```
### 3. Open Localhost
After the server starts, open:
```text
http://localhost:<configured-port>
```
For example:
```text
http://localhost:8080
```
### 4. Access Web Element
```text
HTML elements can be accessed and adjusted in the /resources/web folder
```
---

## Technical Reference
The server-side implementation is based on the **CodeFromScratch HTTP Server tutorial**. The tutorial provides the foundation for implementing a basic HTTP server in Java using socket-based networking and threads.
This implementation adapts that approach for the **Counterfactual** project and provides the server infrastructure needed by the project.
