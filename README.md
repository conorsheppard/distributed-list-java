# 💻 🔁 💻 Distributed List Java

A truly generic, distributed list implementation in Java that supports any key and value types with type-safe serialization.

![Coverage](./badges/jacoco.svg)

## Features

- **Truly Generic**: Both keys and values are generic types (`DistributedList<K, V>`)
- **Type-Safe Serialization**: Custom serializers for any data type
- **Multiple Storage Backends**: In-memory and Redis implementations
- **Thread-Safe**: Concurrent access support
- **Extensible**: Easy to add new storage backends and serializers

## Demo 🎥
In the demo video below, I have open three shell sessions, the two on the right are two independent JShell sessions where I perform operations on `DistributedList.java`, and on the left, I have a shell that I use to exec into the Redis container.
You can see that each JShell session has its own unique JShell Session ID which I generate in the `startup.jsh` script which is executed automatically before the session is opened.
An `init.jsh` script is also executed after the JShell session is open, this script instantiates some classes for us.
The Redis container is running in detached mode in the background and the application can interact with it through the `RedisStoreClient.java` implementation of the `StoreClient.java` interface.
You then see that I can add and remove from the list and the changes are global to any client which holds a reference to the same `DistributedList.java`.
Finally, I switch to the shell session on the left and exec into the Redis Docker container, showing that the changes can also be observed by executing commands directly on the Redis instance.

https://github.com/user-attachments/assets/afdba511-9cc7-4c3f-9ca8-0aefbdbc448e

## Quick Start

### Using GitHub Codespaces

1. Click the "Code" button on this repository
2. Select "Create codespace on main"
3. Wait for the environment to build (includes Java 21, Maven, and Docker)
4. Run the example:

```bash
mvn compile exec:java -Dexec.mainClass="com.conorsheppard.distributedlist.Example"
```

### Local Development

#### Prerequisites
- Java 21
- Maven 3.6+
- Docker (for Redis)

#### Build and Test
```bash
mvn clean test
```

#### Run Example
```bash
mvn compile exec:java -Dexec.mainClass="com.conorsheppard.distributedlist.Example"
```

## Usage Examples

### Basic Usage

```java
// Create store client
StoreClient<String, String> storeClient = new SimpleStoreClient();

// Create serializers
StringSerializer stringSerializer = new StringSerializer();
IntegerSerializer intSerializer = new IntegerSerializer();

// Create a distributed list with Integer keys and String values
DistributedList<Integer, String> list = new DistributedList<>(
    storeClient, "myList", intSerializer, stringSerializer);

// Add elements
list.add("Hello");
list.add("World");

// Get elements
String first = list.get(0); // "Hello"
String second = list.get(1); // "World"

// Remove elements
list.remove(0);
```

### Different Type Combinations

```java
// Integer keys, Integer values
DistributedList<Integer, Integer> intList = new DistributedList<>(
    storeClient, "integers", intSerializer, intSerializer);

// String keys, Integer values  
DistributedList<String, Integer> mixedList = new DistributedList<>(
    storeClient, "mixed", stringSerializer, intSerializer);

// Integer keys, String values
DistributedList<Integer, String> stringList = new DistributedList<>(
    storeClient, "strings", intSerializer, stringSerializer);
```

### Redis Backend

```java
// Create Redis store client
RedisStoreClient redisClient = new RedisStoreClient("localhost", 6379);

// Use with any key/value combination
DistributedList<Integer, String> redisList = new DistributedList<>(
    redisClient, "redisList", intSerializer, stringSerializer);
```

## Architecture

### Core Components

- **`DistributedList<K, V>`**: Main list implementation with generic key and value types
- **`StoreClient<K, V>`**: Interface for storage backends
- **`Serializer<T>`**: Interface for type-safe serialization
- **`SimpleStoreClient`**: In-memory implementation
- **`RedisStoreClient`**: Redis-based implementation

### Serializers

- **`StringSerializer`**: For String types
- **`IntegerSerializer`**: For Integer types
- **Custom Serializers**: Implement `Serializer<T>` for any type

## Testing

The project includes comprehensive tests:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DistributedListTest

# Run with coverage
mvn jacoco:report
```

## Development

### Adding New Serializers

```java
public class MyCustomSerializer implements Serializer<MyType> {
    @Override
    public String serialize(MyType value) {
        // Convert MyType to String
        return value.toString();
    }

    @Override
    public MyType deserialize(String data) {
        // Convert String back to MyType
        return MyType.fromString(data);
    }
}
```

### Adding New Storage Backends

```java
public class MyCustomStoreClient implements StoreClient<String, String> {
    @Override
    public String get(String key) {
        // Implementation
    }

    @Override
    public void set(String key, String value) {
        // Implementation
    }

    @Override
    public int incrementAndGet(String key) {
        // Implementation
    }
}
```