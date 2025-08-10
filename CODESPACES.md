# GitHub Codespaces Guide

This guide will help you get started with the Distributed List Java project in GitHub Codespaces.

## Quick Start

### 1. Create a Codespace

1. Navigate to the repository on GitHub
2. Click the green "Code" button
3. Select the "Codespaces" tab
4. Click "Create codespace on main"

### 2. Wait for Environment Setup

The Codespace will automatically:
- Install Java 24
- Install Maven
- Install Docker (for Redis tests)
- Install VS Code Java extensions
- Build the project

### 3. Start Developing

Once the environment is ready, you can:

#### Run the Example
```bash
mvn compile exec:java -Dexec.mainClass="com.conorsheppard.distributedlist.Example"
```

#### Run Tests
```bash
mvn test
```

#### Build the Project
```bash
mvn clean compile
```

## Environment Features

### Pre-installed Tools
- **Java 24**: Latest Java version
- **Maven 3.9+**: Build tool
- **Docker**: For Redis container tests
- **VS Code Extensions**: Java development tools

### VS Code Extensions
- Java Extension Pack
- Maven for Java
- Debugger for Java
- Test Runner for Java
- Language Support for Java

### Port Forwarding
- **6379**: Redis port (automatically forwarded)

## Development Workflow

### Using VS Code Tasks

The project includes predefined tasks accessible via `Ctrl+Shift+P` → "Tasks: Run Task":

1. **Build Project**: `mvn clean compile`
2. **Run Tests**: `mvn test`
3. **Run Example**: `mvn compile exec:java -Dexec.mainClass="com.conorsheppard.distributedlist.Example"`
4. **Generate Coverage Report**: `mvn jacoco:report`
5. **Clean and Build**: `mvn clean package`

### Debugging

Use the debug configurations in `.codespaces/launch.json`:

1. **Run Example**: Debug the main example
2. **Debug Tests**: Debug all tests
3. **Debug DistributedListTest**: Debug specific test class

### Code Quality

The environment includes:
- **Auto-formatting**: Google Java Style
- **Import organization**: Automatic on save
- **Null analysis**: Automatic compilation checks

## Testing with Redis

The project includes Redis integration tests. In Codespaces:

1. **Run Redis Tests**: `mvn test -Dtest=RedisStoreClientTest`
2. **Manual Redis Testing**: 
   ```bash
   # Start Redis container
   docker run -d -p 6379:6379 redis:latest
   
   # Run example with Redis
   mvn compile exec:java -Dexec.mainClass="com.conorsheppard.distributedlist.Example"
   ```

## Troubleshooting

### Common Issues

1. **Maven not found**: Restart the Codespace
2. **Java version issues**: Check with `java -version`
3. **Docker not available**: Check with `docker --version`

### Useful Commands

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Docker
docker --version

# Clean and rebuild
mvn clean compile

# Run specific test
mvn test -Dtest=DistributedListTest

# Generate coverage report
mvn jacoco:report
```

## Performance Tips

1. **Use VS Code Tasks**: Faster than typing commands
2. **Enable Auto-save**: Automatic formatting and import organization
3. **Use Debug Configurations**: Faster than running from terminal
4. **Leverage IntelliSense**: Auto-completion and error detection

## Next Steps

1. **Explore the Code**: Start with `DistributedList.java`
2. **Run Examples**: Try different type combinations
3. **Add Custom Serializers**: Implement `Serializer<T>` for your types
4. **Add Storage Backends**: Implement `StoreClient<K, V>`
5. **Write Tests**: Add tests for your custom implementations

## Support

- **Issues**: Create GitHub issues for bugs or feature requests
- **Discussions**: Use GitHub Discussions for questions
- **Documentation**: Check the main README.md for detailed usage 