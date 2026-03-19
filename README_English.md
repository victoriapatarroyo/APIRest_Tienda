# 🏪 Tienda Victoria — REST API

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square&logo=hibernate)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=flat-square&logo=apachemaven)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=flat-square)

REST API for customer management of Tienda Victoria, built with **Spring Boot**, **JPA/Hibernate** and **MySQL**.

> 🔗 **Repository:** [github.com/victoriapatarroyo/APIRest_Tienda](https://github.com/victoriapatarroyo/APIRest_Tienda)

---

## 📦 Detailed Package Structure

The project follows a **Layered Architecture**, where each layer has a specific responsibility and communicates only with the adjacent layer.

```
src/main/java/com/tienda/victoria/
│
├── 📁 controller/
│   └── ClienteController.java      # Presentation layer — receives and responds to HTTP requests
│
├── 📁 service/
│   └── ClienteService.java         # Business layer — contains application logic
│
├── 📁 repository/
│   └── ClienteRepository.java      # Data layer — accesses and manipulates the database
│
├── 📁 model/
│   └── Cliente.java                # JPA Entity — represents the database table
│
├── 📁 dto/
│   └── ClienteDTO.java             # Transfer object — data that travels between layers
│
└── VictoriaApplication.java        # Main class — application entry point
```

### Why this architecture?

| Layer | Responsibility | Communicates with |
|-------|---------------|-------------------|
| `Controller` | Receive HTTP requests and return responses | `Service` |
| `Service` | Apply business rules and transform data | `Repository` + `DTO` |
| `Repository` | Execute CRUD operations on the database | `Model` + MySQL |
| `Model` | Represent the table structure in the DB | `Repository` |
| `DTO` | Transfer only the necessary data | `Controller` + `Service` |

---

## 🧩 Explanation of each Java class

### 📄 `VictoriaApplication.java`
Main class that starts the entire Spring Boot application.
```java
@SpringBootApplication  // Enables auto-configuration, component scanning and configuration
public class VictoriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(VictoriaApplication.class, args);
    }
}
```

---

### 📄 `Cliente.java` — JPA Entity
Directly represents the `cliente` table in MySQL. Each attribute is a column.

```java
@Entity                      // Tells JPA that this class is a database table
@Table(name = "cliente")     // Maps to the table named "cliente" in MySQL
public class Cliente {

    @Id                                                    // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // AUTO_INCREMENT in MySQL
    @Column(name = "idCliente")
    private Integer idCliente;

    @Column(name = "nombreCliente", nullable = false, length = 50)  // NOT NULL, VARCHAR(50)
    private String nombreCliente;

    @Column(name = "apellidoCliente", nullable = false, length = 50)
    private String apellidoCliente;

    @Column(name = "tipoDocumento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numeroDocumento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(name = "telefonoCliente", nullable = false, length = 15)
    private String telefonoCliente;

    @Column(name = "correoCliente", nullable = false, length = 50)
    private String correoCliente;

    // Empty constructor required by JPA
    public Cliente() {}

    // Full constructor to easily create objects
    public Cliente(Integer idCliente, String nombreCliente, ...) { ... }

    // Getters and Setters — required for JPA to read and write values
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    // ... remaining getters and setters
}
```

---

### 📄 `ClienteDTO.java` — Data Transfer Object
Class that defines what data is sent and received in HTTP requests. It has no database annotations — it is just a data container.

```java
public class ClienteDTO {

    // Same fields as the entity, but WITHOUT JPA annotations
    private Integer idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefonoCliente;
    private String correoCliente;

    // Empty constructor — required for Spring to deserialize the request body JSON
    public ClienteDTO() {}

    // Full constructor — useful for quickly creating DTOs in the Service
    public ClienteDTO(Integer idCliente, String nombreCliente, ...) { ... }

    // Getters and Setters — required for JSON serialization/deserialization
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    // ... remaining getters and setters
}
```

> 💡 **Why use a DTO instead of the Entity directly?**
> The DTO protects the entity from direct exposure, allows control over which fields are shown to the client, and avoids serialization issues in entity relationships.

---

### 📄 `ClienteRepository.java` — Repository
Interface that extends `JpaRepository` to access all CRUD methods without writing any SQL.

```java
@Repository  // Marks this interface as a data access component
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // JpaRepository<T, ID> where:
    // T   = entity type (Cliente)
    // ID  = identifier type (Integer, because idCliente is Integer)

    // Automatically inherited methods:
    // findAll()        → SELECT * FROM cliente
    // findById(id)     → SELECT * FROM cliente WHERE idCliente = ?
    // save(cliente)    → INSERT or UPDATE depending on whether it has an ID or not
    // deleteById(id)   → DELETE FROM cliente WHERE idCliente = ?
    // existsById(id)   → SELECT COUNT(*) FROM cliente WHERE idCliente = ?
}
```

---

### 📄 `ClienteService.java` — Service
Contains all the business logic. Receives data from the Controller, processes it and communicates with the Repository. Also converts between `Cliente` (entity) and `ClienteDTO`.

```java
@Service  // Marks this class as a business logic component
public class ClienteService {

    @Autowired  // Dependency injection — Spring provides the Repository automatically
    private ClienteRepository clienteRepository;

    // 📋 List all — converts each entity to DTO using Stream
    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::convertirADTO)   // For each Cliente, calls convertirADTO()
                .collect(Collectors.toList());
    }

    // 🔍 Find by ID — throws exception if not found
    public ClienteDTO buscarPorId(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            return convertirADTO(cliente.get());
        }
        throw new RuntimeException("Cliente no encontrado con ID: " + id);
    }

    // ➕ Save — converts DTO to entity, saves and returns DTO with generated ID
    public ClienteDTO guardarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertirAEntidad(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirADTO(clienteGuardado);
    }

    // ✏️ Update — finds the record, updates its fields and saves
    public ClienteDTO actualizarCliente(Integer id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNombreCliente(clienteDTO.getNombreCliente());
            // ... updates all fields
            return convertirADTO(clienteRepository.save(cliente));
        }
        throw new RuntimeException("Cliente no encontrado con ID: " + id);
    }

    // 🗑️ Delete — verifies existence before deleting
    public void eliminarCliente(Integer id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    // 🔄 Conversion Entity → DTO
    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombreCliente(cliente.getNombreCliente());
        // ... copies all fields
        return dto;
    }

    // 🔄 Conversion DTO → Entity
    private Cliente convertirAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombreCliente(dto.getNombreCliente());
        // ... copies all fields (without setIdCliente, it is AUTO_INCREMENT)
        return cliente;
    }
}
```

---

### 📄 `ClienteController.java` — Controller
Exposes the HTTP REST endpoints. Receives requests, calls the Service and returns the appropriate response.

```java
@RestController              // Combines @Controller + @ResponseBody (automatically responds with JSON)
@RequestMapping("/api/clientes")  // Base URL for all endpoints
@CrossOrigin(origins = "*")  // Allows requests from any origin (frontend)
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping                          // GET /api/clientes
    public ResponseEntity<?> listarClientes() { ... }

    @GetMapping("/{id}")                 // GET /api/clientes/{id}
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) { ... }

    @PostMapping                         // POST /api/clientes
    public ResponseEntity<?> guardarCliente(@RequestBody ClienteDTO clienteDTO) { ... }

    @PutMapping("/{id}")                 // PUT /api/clientes/{id}
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id,
                                               @RequestBody ClienteDTO dto) { ... }

    @DeleteMapping("/{id}")              // DELETE /api/clientes/{id}
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) { ... }
}
```

> 💡 **Key annotations:**
> - `@PathVariable` → captures the `{id}` from the URL
> - `@RequestBody` → converts the body JSON into a Java object
> - `ResponseEntity<?>` → allows returning both objects and text messages with their HTTP status

---

## 🔄 Complete Request Flow

### Example: `POST /api/clientes` — Create a customer

```
┌─────────┐      HTTP POST + JSON       ┌────────────────────┐
│ POSTMAN │ ──────────────────────────► │ ClienteController  │
└─────────┘                             └────────┬───────────┘
                                                 │ @RequestBody
                                                 │ JSON → ClienteDTO
                                                 ▼
                                        ┌────────────────────┐
                                        │  ClienteService    │
                                        │ 1. Receives DTO    │
                                        │ 2. DTO → Entity    │
                                        │ 3. Calls repository│
                                        └────────┬───────────┘
                                                 │ save(cliente)
                                                 ▼
                                        ┌────────────────────┐
                                        │ ClienteRepository  │
                                        │  INSERT INTO       │
                                        │  cliente (...)     │
                                        └────────┬───────────┘
                                                 │ Entity with ID
                                                 ▼
                                        ┌────────────────────┐
                                        │  ClienteService    │
                                        │  Entity → DTO      │
                                        └────────┬───────────┘
                                                 │ ClienteDTO
                                                 ▼
                                        ┌────────────────────┐
                                        │ ClienteController  │
                                        │ 201 CREATED + JSON │
                                        └────────┬───────────┘
                                                 │ JSON Response
                                                 ▼
                                             ┌─────────┐
                                             │ POSTMAN │
                                             └─────────┘
```

### Flow summary by operation

| Operation | Controller | Service | Repository | DB |
|-----------|-----------|---------|------------|-----|
| `GET` list all | Receives request | Calls `findAll()` | `SELECT *` | Returns records |
| `GET` by ID | Receives `{id}` | Calls `findById()` | `SELECT WHERE id` | Returns 1 record |
| `POST` create | Receives JSON | Converts and saves | `INSERT` | Creates record |
| `PUT` update | Receives `{id}` + JSON | Finds, updates and saves | `UPDATE` | Updates record |
| `DELETE` delete | Receives `{id}` | Verifies and deletes | `DELETE` | Deletes record |

---

## 🌐 Endpoints and JSON Responses

Base URL: `http://localhost:8080/api/clientes`

---

### 📋 GET `/api/clientes` — List all customers

**✅ 200 OK — With records:**
```json
[
    {
        "idCliente": 1,
        "nombreCliente": "Juan",
        "apellidoCliente": "Pérez",
        "tipoDocumento": "CC",
        "numeroDocumento": "123456789",
        "telefonoCliente": "3001234567",
        "correoCliente": "juan.perez@gmail.com"
    },
    {
        "idCliente": 2,
        "nombreCliente": "María",
        "apellidoCliente": "González",
        "tipoDocumento": "CC",
        "numeroDocumento": "987654321",
        "telefonoCliente": "3109876543",
        "correoCliente": "maria.gonzalez@gmail.com"
    }
]
```

**❌ 404 Not Found — Empty table:**
```json
"No hay clientes registrados"
```

---

### 🔍 GET `/api/clientes/{id}` — Find by ID

**✅ 200 OK — Customer found:**
```json
{
    "idCliente": 1,
    "nombreCliente": "Juan",
    "apellidoCliente": "Pérez",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "telefonoCliente": "3001234567",
    "correoCliente": "juan.perez@gmail.com"
}
```

**❌ 404 Not Found — ID does not exist:**
```json
"Cliente no encontrado con ID: 99"
```

---

### ➕ POST `/api/clientes` — Create customer

**📤 Required body:**
```json
{
    "nombreCliente": "Juan",
    "apellidoCliente": "Pérez",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "telefonoCliente": "3001234567",
    "correoCliente": "juan.perez@gmail.com"
}
```

> ⚠️ Do not send `idCliente` — it is AUTO_INCREMENT, MySQL generates it automatically.

**✅ 201 Created:**
```json
{
    "idCliente": 1,
    "nombreCliente": "Juan",
    "apellidoCliente": "Pérez",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "telefonoCliente": "3001234567",
    "correoCliente": "juan.perez@gmail.com"
}
```

**❌ 400 Bad Request:**
```json
"Error al guardar el cliente"
```

---

### ✏️ PUT `/api/clientes/{id}` — Update customer

**📤 Required body:**
```json
{
    "nombreCliente": "Juan",
    "apellidoCliente": "García",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "telefonoCliente": "3009876543",
    "correoCliente": "juan.garcia@gmail.com"
}
```

**✅ 200 OK:**
```json
{
    "idCliente": 1,
    "nombreCliente": "Juan",
    "apellidoCliente": "García",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "telefonoCliente": "3009876543",
    "correoCliente": "juan.garcia@gmail.com"
}
```

**❌ 404 Not Found:**
```json
"Error al actualizar cliente con ID: 99"
```

---

### 🗑️ DELETE `/api/clientes/{id}` — Delete customer

**✅ 204 No Content:**
```
(No response body)
```

**❌ 404 Not Found:**
```json
"Cliente no encontrado con ID: 99"
```

---

## 🗂️ Database

### `cliente` table structure

```sql
CREATE TABLE IF NOT EXISTS `cliente` (
  `id_cliente`       INT NOT NULL AUTO_INCREMENT,
  `nombre_cliente`   VARCHAR(50) NOT NULL,
  `apellido_cliente` VARCHAR(50) NOT NULL,
  `tipo_documento`   VARCHAR(20) NOT NULL,
  `numero_documento` VARCHAR(20) NOT NULL,
  `telefono_cliente` VARCHAR(15) NOT NULL,
  `correo_cliente`   VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_cliente`)
);
```

### Sample data

```sql
INSERT INTO `cliente` (`nombre_cliente`, `apellido_cliente`, `tipo_documento`, `numero_documento`, `telefono_cliente`, `correo_cliente`) VALUES
('Juan',      'Pérez',     'CC', '123456789', '3001234567', 'juan.perez@gmail.com'),
('María',     'González',  'CC', '987654321', '3109876543', 'maria.gonzalez@gmail.com'),
('Carlos',    'Rodríguez', 'CC', '456789123', '3204567891', 'carlos.rodriguez@gmail.com'),
('Ana',       'Martínez',  'TI', '321654987', '3153216549', 'ana.martinez@gmail.com'),
('Luis',      'García',    'CC', '654987321', '3006549873', 'luis.garcia@gmail.com'),
('Laura',     'López',     'CC', '789123456', '3187891234', 'laura.lopez@gmail.com'),
('Jorge',     'Hernández', 'CE', '159753486', '3051597534', 'jorge.hernandez@gmail.com'),
('Sofía',     'Díaz',      'CC', '753951468', '3117539514', 'sofia.diaz@gmail.com'),
('Andrés',    'Torres',    'CC', '852741963', '3208527419', 'andres.torres@gmail.com'),
('Valentina', 'Ramírez',   'TI', '963852741', '3019638527', 'valentina.ramirez@gmail.com');
```

---

## ⚙️ Dependency Management — `pom.xml`

This project uses **Maven** as its dependency manager. Below is the complete `pom.xml` with a description of each dependency.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <!-- Parent: inherits default Spring Boot configurations and plugin versions -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.4</version>
        <relativePath/>
    </parent>

    <!-- Project identification -->
    <groupId>com.tienda</groupId>
    <artifactId>victoria</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>

        <!-- Spring Web: enables REST controllers, HTTP request handling and embedded Tomcat -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Data JPA: provides JpaRepository, entity mapping and Hibernate integration -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- MySQL Connector: JDBC driver to connect the app to MySQL database -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>  <!-- Only needed at runtime, not at compile time -->
        </dependency>

        <!-- DevTools: enables hot reload during development — changes apply without restarting -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>  <!-- Not included in the final build -->
        </dependency>

        <!-- Test: includes JUnit 5, Mockito and Spring Test for unit and integration testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>  <!-- Only available during test execution -->
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <!-- Spring Boot Maven Plugin: packages the app as an executable JAR -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

### Dependency summary

| Dependency | Version | Scope | Purpose |
|------------|---------|-------|---------|
| `spring-boot-starter-parent` | 3.2.4 | Parent | Base configuration and managed versions |
| `spring-boot-starter-web` | Managed | Compile | REST controllers + embedded Tomcat |
| `spring-boot-starter-data-jpa` | Managed | Compile | JPA + Hibernate ORM |
| `mysql-connector-j` | Managed | Runtime | JDBC driver for MySQL |
| `spring-boot-devtools` | Managed | Runtime | Hot reload in development |
| `spring-boot-starter-test` | Managed | Test | JUnit 5 + Mockito |
| `spring-boot-maven-plugin` | Managed | Build | Packages app as executable JAR |

> 💡 **Managed versions** means the version is automatically handled by `spring-boot-starter-parent` — no need to specify it manually for each dependency.

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/victoriapatarroyo/APIRest_Tienda.git
cd victoria
```

### 2. Create the database in MySQL
```sql
CREATE DATABASE IF NOT EXISTS tienda;
```

### 3. Configure `application.properties`
```properties
spring.application.name=victoria

spring.datasource.url=jdbc:mysql://localhost:3306/tienda
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

### 4. Run from IntelliJ IDEA
```
Run → VictoriaApplication.java
```

---

## 🏗️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming language |
| Spring Boot 3.2.4 | Main framework |
| Spring Data JPA | Data access |
| Hibernate | ORM |
| MySQL 8.0 | Database |
| Maven | Dependency management |
| IntelliJ IDEA | Development IDE |
| Postman | API testing |

---

## 🔮 Future Improvements

- Add more entities (Products, Sales, etc.)
- Input validations with `@Valid` and `@NotBlank`
- Global exception handling with `@ControllerAdvice`
- Authentication with Spring Security + JWT
- Automatic API documentation with Swagger/OpenAPI
- Pagination for the customer list

---

## 👩‍💻 Author

**Victoria Eugenia Patarroyo Villamil**
Full Stack Developer learning and building practical and scalable web applications.

[![GitHub](https://img.shields.io/badge/GitHub-victoriapatarroyo-black?style=flat-square&logo=github)](https://github.com/victoriapatarroyo)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Victoria%20Patarroyo-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/victoriaeugeniapatarroyo/)

---

## 📄 License

This project is licensed under the **MIT License** — feel free to use, modify and share it.
