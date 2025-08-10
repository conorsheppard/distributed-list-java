# 💻 🔁 💻 Distributed List Java

A truly generic, distributed list implementation in Java that supports any key and value types with type-safe serialization.

![Coverage](./badges/jacoco.svg)

## Features

- **Truly Generic**: Both keys and values are generic types (`DistributedList<K, V>`)
- **Type-Safe Serialization**: Custom serializers for any data type
- **Multiple Storage Backends**: In-memory and Redis implementations
- **Thread-Safe**: Concurrent access support
- **Extensible**: Easy to add new storage backends and serializers

## Quick Start

### Using GitHub Codespaces

1. Click the "Code" button on this repository
2. Select "Create codespace on main"
3. Wait for the environment to build (includes Java 24, Maven, and Docker)
4. Run the example:

```bash
mvn compile exec:java -Dexec.mainClass="com.conorsheppard.distributedlist.Example"
```

### Local Development

#### Prerequisites
- Java 24
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