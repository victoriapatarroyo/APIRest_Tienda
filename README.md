# 🏪 Tienda Victoria — API REST

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square&logo=hibernate)
![Status](https://img.shields.io/badge/Status-En%20Desarrollo-yellow?style=flat-square)

API REST para la gestión de clientes de la Tienda Victoria, desarrollada con **Spring Boot**, **JPA/Hibernate** y **MySQL**.

> 🔗 **Repositorio:** [github.com/victoriapatarroyo/APIRest_Tienda](https://github.com/victoriapatarroyo/APIRest_Tienda/)

---

## 📸 Estructura del Proyecto

```
src/main/java/com/tienda/victoria/
│
├── controller/
│   └── ClienteController.java      # Endpoints REST
├── service/
│   └── ClienteService.java         # Lógica de negocio
├── repository/
│   └── ClienteRepository.java      # Acceso a datos
├── model/
│   └── Cliente.java                # Entidad JPA
├── dto/
│   └── ClienteDTO.java             # Objeto de transferencia
└── VictoriaApplication.java        # Clase principal
```

---

## ✨ Funcionalidades

- ➕ Crear nuevos clientes
- 📋 Listar todos los clientes
- 🔍 Buscar cliente por ID
- ✏️ Actualizar datos de un cliente
- 🗑️ Eliminar un cliente
- 🔔 Mensajes de error descriptivos cuando no se encuentra un registro

---

## 🚀 Cómo ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/victoriapatarroyo/victoria.git
cd victoria
```

### 2. Configurar la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS tienda;
```

### 3. Configurar `application.properties`

```properties
spring.application.name=victoria

# Conexión a MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/tienda
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
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

## 🗂️ Base de Datos

### Estructura de la tabla `cliente`

```sql
CREATE TABLE IF NOT EXISTS `cliente` (
  `idCliente`       INT NOT NULL AUTO_INCREMENT,
  `nombreCliente`   VARCHAR(50) NOT NULL,
  `apellidoCliente` VARCHAR(50) NOT NULL,
  `tipoDocumento`   VARCHAR(20) NOT NULL,
  `numeroDocumento` VARCHAR(20) NOT NULL,
  `telefonoCliente` VARCHAR(15) NOT NULL,
  `correoCliente`   VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idCliente`)
);
```

### Datos de prueba

```sql
INSERT INTO `cliente` (`nombreCliente`, `apellidoCliente`, `tipoDocumento`, `numeroDocumento`, `telefonoCliente`, `correoCliente`) VALUES
('Juan',       'Pérez',     'CC',  '123456789', '3001234567', 'juan.perez@gmail.com'),
('María',      'González',  'CC',  '987654321', '3109876543', 'maria.gonzalez@gmail.com'),
('Carlos',     'Rodríguez', 'CC',  '456789123', '3204567891', 'carlos.rodriguez@gmail.com'),
('Ana',        'Martínez',  'TI',  '321654987', '3153216549', 'ana.martinez@gmail.com'),
('Luis',       'García',    'CC',  '654987321', '3006549873', 'luis.garcia@gmail.com'),
('Laura',      'López',     'CC',  '789123456', '3187891234', 'laura.lopez@gmail.com'),
('Jorge',      'Hernández', 'CE',  '159753486', '3051597534', 'jorge.hernandez@gmail.com'),
('Sofía',      'Díaz',      'CC',  '753951468', '3117539514', 'sofia.diaz@gmail.com'),
('Andrés',     'Torres',    'CC',  '852741963', '3208527419', 'andres.torres@gmail.com'),
('Valentina',  'Ramírez',   'TI',  '963852741', '3019638527', 'valentina.ramirez@gmail.com');
```

---

## 🌐 Endpoints disponibles

Base URL: `http://localhost:8080/api/clientes`

| Método | Endpoint | Descripción | HTTP Status |
|--------|----------|-------------|-------------|
| `GET` | `/api/clientes` | Listar todos los clientes | `200 OK` / `404` |
| `GET` | `/api/clientes/{id}` | Buscar cliente por ID | `200 OK` / `404` |
| `POST` | `/api/clientes` | Crear nuevo cliente | `201 Created` |
| `PUT` | `/api/clientes/{id}` | Actualizar cliente | `200 OK` / `404` |
| `DELETE` | `/api/clientes/{id}` | Eliminar cliente | `204 No Content` / `404` |

---

## 📬 Ejemplos con Postman

### ➕ POST — Crear cliente

```
POST http://localhost:8080/api/clientes
Content-Type: application/json
```

```json
{
    "nombreCliente": "Juan",
    "apellidoCleba": "Pérez",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "telefonoCliente": "3001234567",
    "correoCliente": "juan.perez@gmail.com"
}
```

### 🔍 GET — Buscar por ID

```
GET http://localhost:8080/api/clientes/1
```

### ✏️ PUT — Actualizar cliente

```
PUT http://localhost:8080/api/clientes/1
Content-Type: application/json
```

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

### 🗑️ DELETE — Eliminar cliente

```
DELETE http://localhost:8080/api/clientes/1
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
