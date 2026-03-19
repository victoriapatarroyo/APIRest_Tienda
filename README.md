# 🏪 Tienda Victoria — API REST

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square&logo=hibernate)
![Status](https://img.shields.io/badge/Status-En%20Desarrollo-yellow?style=flat-square)

API REST para la gestión de clientes de la Tienda Victoria, desarrollada con **Spring Boot**, **JPA/Hibernate** y **MySQL**.

> 🔗 **Repositorio:** [github.com/victoriapatarroyo/victoria](https://github.com/victoriapatarroyo/victoria)

---

## 📦 Estructura de Paquetes Detallada

El proyecto sigue una **arquitectura en capas** (Layered Architecture), donde cada capa tiene una responsabilidad específica y se comunica únicamente con la capa adyacente.

```
src/main/java/com/tienda/victoria/
│
├── 📁 controller/
│   └── ClienteController.java      # Capa de presentación — recibe y responde peticiones HTTP
│
├── 📁 service/
│   └── ClienteService.java         # Capa de negocio — contiene la lógica de la aplicación
│
├── 📁 repository/
│   └── ClienteRepository.java      # Capa de datos — accede y manipula la base de datos
│
├── 📁 model/
│   └── Cliente.java                # Entidad JPA — representa la tabla en la base de datos
│
├── 📁 dto/
│   └── ClienteDTO.java             # Objeto de transferencia — datos que viajan entre capas
│
└── VictoriaApplication.java        # Clase principal — punto de entrada de la aplicación
```

### ¿Por qué esta arquitectura?

| Capa | Responsabilidad | Se comunica con |
|------|----------------|-----------------|
| `Controller` | Recibir peticiones HTTP y devolver respuestas | `Service` |
| `Service` | Aplicar reglas de negocio y transformar datos | `Repository` + `DTO` |
| `Repository` | Ejecutar operaciones CRUD en la base de datos | `Model` + MySQL |
| `Model` | Representar la estructura de la tabla en BD | `Repository` |
| `DTO` | Transferir solo los datos necesarios | `Controller` + `Service` |

---

## 🧩 Explicación de cada clase Java

### 📄 `VictoriaApplication.java`
Clase principal que arranca toda la aplicación Spring Boot.
```java
@SpringBootApplication  // Habilita autoconfiguración, escaneo de componentes y configuración
public class VictoriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(VictoriaApplication.class, args);
    }
}
```

---

### 📄 `Cliente.java` — Entidad JPA
Representa directamente la tabla `cliente` en MySQL. Cada atributo es una columna.

```java
@Entity                      // Le dice a JPA que esta clase es una tabla de BD
@Table(name = "cliente")     // Mapea con la tabla llamada "cliente" en MySQL
public class Cliente {

    @Id                                                    // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // AUTO_INCREMENT en MySQL
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

    // Constructor vacío requerido por JPA
    public Cliente() {}

    // Constructor completo para crear objetos fácilmente
    public Cliente(Integer idCliente, String nombreCliente, ...) { ... }

    // Getters y Setters — necesarios para que JPA lea y escriba los valores
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    // ... resto de getters y setters
}
```

---

### 📄 `ClienteDTO.java` — Objeto de Transferencia
Clase que define qué datos se envían y reciben en las peticiones HTTP. No tiene anotaciones de base de datos, es solo un contenedor de datos.

```java
public class ClienteDTO {

    // Mismos campos que la entidad, pero SIN anotaciones JPA
    private Integer idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefonoCliente;
    private String correoCliente;

    // Constructor vacío — necesario para que Spring deserialice el JSON del body
    public ClienteDTO() {}

    // Constructor completo — útil para crear DTOs rápidamente en el Service
    public ClienteDTO(Integer idCliente, String nombreCliente, ...) { ... }

    // Getters y Setters — necesarios para serializar/deserializar JSON
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    // ... resto de getters y setters
}
```

> 💡 **¿Por qué usar DTO y no la Entidad directamente?**
> El DTO protege la entidad de exposición directa, permite controlar qué campos se muestran al cliente y evita problemas de serialización en relaciones entre entidades.

---

### 📄 `ClienteRepository.java` — Repositorio
Interfaz que extiende `JpaRepository` para tener acceso a todos los métodos CRUD sin escribir código SQL.

```java
@Repository  // Marca esta interfaz como componente de acceso a datos
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // JpaRepository<T, ID> donde:
    // T   = tipo de la entidad (Cliente)
    // ID  = tipo del identificador (Integer, porque idCliente es Integer)

    // Métodos heredados automáticamente:
    // findAll()        → SELECT * FROM cliente
    // findById(id)     → SELECT * FROM cliente WHERE idCliente = ?
    // save(cliente)    → INSERT o UPDATE según si tiene ID o no
    // deleteById(id)   → DELETE FROM cliente WHERE idCliente = ?
    // existsById(id)   → SELECT COUNT(*) FROM cliente WHERE idCliente = ?
}
```

---

### 📄 `ClienteService.java` — Servicio
Contiene toda la lógica de negocio. Recibe datos del Controller, los procesa y se comunica con el Repository. También convierte entre `Cliente` (entidad) y `ClienteDTO`.

```java
@Service  // Marca esta clase como componente de lógica de negocio
public class ClienteService {

    @Autowired  // Inyección de dependencia — Spring provee el Repository automáticamente
    private ClienteRepository clienteRepository;

    // 📋 Listar todos — convierte cada entidad a DTO usando Stream
    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::convertirADTO)   // Por cada Cliente, llama convertirADTO()
                .collect(Collectors.toList());
    }

    // 🔍 Buscar por ID — lanza excepción si no existe
    public ClienteDTO buscarPorId(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            return convertirADTO(cliente.get());
        }
        throw new RuntimeException("Cliente no encontrado con ID: " + id);
    }

    // ➕ Guardar — convierte DTO a entidad, guarda y retorna el DTO con ID generado
    public ClienteDTO guardarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertirAEntidad(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirADTO(clienteGuardado);
    }

    // ✏️ Actualizar — busca el registro, actualiza sus campos y guarda
    public ClienteDTO actualizarCliente(Integer id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNombreCliente(clienteDTO.getNombreCliente());
            // ... actualiza todos los campos
            return convertirADTO(clienteRepository.save(cliente));
        }
        throw new RuntimeException("Cliente no encontrado con ID: " + id);
    }

    // 🗑️ Eliminar — verifica existencia antes de eliminar
    public void eliminarCliente(Integer id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    // 🔄 Conversión Entidad → DTO
    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombreCliente(cliente.getNombreCliente());
        // ... copia todos los campos
        return dto;
    }

    // 🔄 Conversión DTO → Entidad
    private Cliente convertirAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombreCliente(dto.getNombreCliente());
        // ... copia todos los campos (sin setIdCliente, es AUTO_INCREMENT)
        return cliente;
    }
}
```

---

### 📄 `ClienteController.java` — Controlador
Expone los endpoints HTTP REST. Recibe las peticiones, llama al Service y devuelve la respuesta apropiada.

```java
@RestController              // Combina @Controller + @ResponseBody (responde JSON automáticamente)
@RequestMapping("/api/clientes")  // URL base para todos los endpoints
@CrossOrigin(origins = "*")  // Permite peticiones desde cualquier origen (frontend)
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

> 💡 **Anotaciones clave:**
> - `@PathVariable` → captura el `{id}` de la URL
> - `@RequestBody` → convierte el JSON del body en un objeto Java
> - `ResponseEntity<?>` → permite retornar tanto objetos como mensajes de texto con su HTTP status

---

## 🔄 Flujo completo de una petición

### Ejemplo: `POST /api/clientes` — Crear un cliente

```
┌─────────┐      HTTP POST + JSON       ┌────────────────────┐
│ POSTMAN │ ──────────────────────────► │ ClienteController  │
└─────────┘                             └────────┬───────────┘
                                                 │ @RequestBody
                                                 │ JSON → ClienteDTO
                                                 ▼
                                        ┌────────────────────┐
                                        │  ClienteService    │
                                        │ 1. Recibe DTO      │
                                        │ 2. DTO → Entidad   │
                                        │ 3. Llama repository│
                                        └────────┬───────────┘
                                                 │ save(cliente)
                                                 ▼
                                        ┌────────────────────┐
                                        │ ClienteRepository  │
                                        │  INSERT INTO       │
                                        │  cliente (...)     │
                                        └────────┬───────────┘
                                                 │ Cliente con ID
                                                 ▼
                                        ┌────────────────────┐
                                        │  ClienteService    │
                                        │  Entidad → DTO     │
                                        └────────┬───────────┘
                                                 │ ClienteDTO
                                                 ▼
                                        ┌────────────────────┐
                                        │ ClienteController  │
                                        │ 201 CREATED + JSON │
                                        └────────┬───────────┘
                                                 │ Respuesta JSON
                                                 ▼
                                             ┌─────────┐
                                             │ POSTMAN │
                                             └─────────┘
```

### Resumen del flujo por operación

| Operación | Controller | Service | Repository | BD |
|-----------|-----------|---------|------------|-----|
| `GET` listar | Recibe petición | Llama `findAll()` | `SELECT *` | Retorna registros |
| `GET` por ID | Recibe `{id}` | Llama `findById()` | `SELECT WHERE id` | Retorna 1 registro |
| `POST` crear | Recibe JSON | Convierte y guarda | `INSERT` | Crea registro |
| `PUT` actualizar | Recibe `{id}` + JSON | Busca, modifica y guarda | `UPDATE` | Actualiza registro |
| `DELETE` eliminar | Recibe `{id}` | Verifica y elimina | `DELETE` | Elimina registro |

---

## 🌐 Endpoints y Respuestas JSON

Base URL: `http://localhost:8080/api/clientes`

---

### 📋 GET `/api/clientes` — Listar todos

**✅ 200 OK — Con registros:**
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

**❌ 404 Not Found — Tabla vacía:**
```json
"No hay clientes registrados"
```

---

### 🔍 GET `/api/clientes/{id}` — Buscar por ID

**✅ 200 OK — Cliente encontrado:**
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

**❌ 404 Not Found — ID no existe:**
```json
"Cliente no encontrado con ID: 99"
```

---

### ➕ POST `/api/clientes` — Crear cliente

**📤 Body requerido:**
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

> ⚠️ No enviar `idCliente` — es AUTO_INCREMENT, MySQL lo genera automáticamente.

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

### ✏️ PUT `/api/clientes/{id}` — Actualizar cliente

**📤 Body requerido:**
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

### 🗑️ DELETE `/api/clientes/{id}` — Eliminar cliente

**✅ 204 No Content:**
```
(Sin cuerpo en la respuesta)
```

**❌ 404 Not Found:**
```json
"Cliente no encontrado con ID: 99"
```

---

## 🗂️ Base de Datos

### Estructura de la tabla `cliente`

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

### Datos de prueba

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

## 🚀 Cómo ejecutar el proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/victoriapatarroyo/APIRest_Tienda.git
cd victoria
```

### 2. Crear la base de datos en MySQL
```sql
CREATE DATABASE IF NOT EXISTS tienda;
```

### 3. Configurar `application.properties`
```properties
spring.application.name=victoria

spring.datasource.url=jdbc:mysql://localhost:3306/tienda
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

### 4. Ejecutar desde IntelliJ IDEA
```
Run → VictoriaApplication.java
```

---

## 🏗️ Tech Stack

| Tecnología | Uso |
|------------|-----|
| Java 17 | Lenguaje de programación |
| Spring Boot 4.0.4 | Framework principal |
| Spring Data JPA | Acceso a datos |
| Hibernate | ORM |
| MySQL 8.0 | Base de datos |
| Maven | Gestión de dependencias |
| IntelliJ IDEA | IDE de desarrollo |
| Postman | Pruebas de API |

---

## 🔮 Mejoras futuras

- Agregar más entidades (Productos, Ventas, etc.)
- Validaciones con `@Valid` y `@NotBlank`
- Manejo global de excepciones con `@ControllerAdvice`
- Autenticación con Spring Security + JWT
- Documentación automática con Swagger/OpenAPI
- Paginación en el listado de clientes

---

## 👩‍💻 Autora

**Victoria Eugenia Patarroyo Villamil**
Desarrolladora Fullstack aprendiendo y construyendo aplicaciones prácticas y escalables.

[![GitHub](https://img.shields.io/badge/GitHub-victoriapatarroyo-black?style=flat-square&logo=github)](https://github.com/victoriapatarroyo)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Victoria%20Patarroyo-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/victoriaeugeniapatarroyo/)

---

## 📄 Licencia

Este proyecto está bajo la licencia **MIT** — siéntete libre de usarlo, modificarlo y compartirlo.
