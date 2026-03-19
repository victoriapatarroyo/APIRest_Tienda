package com.tienda.victoria.controller;

import com.tienda.victoria.dto.ClienteDTO;
import com.tienda.victoria.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // GET - Listar todos los clientes
    @GetMapping
    public ResponseEntity<?> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay clientes registrados");
        }
        return ResponseEntity.ok(clientes);
    }

    // GET - Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            ClienteDTO cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente no encontrado con ID: " + id);
        }
    }

    // POST - Crear nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardarCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO clienteGuardado = clienteService.guardarCliente(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al guardar el cliente");
        }
    }

    // PUT - Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id,
                                               @RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO clienteActualizado = clienteService.actualizarCliente(id, clienteDTO);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error al actualizar cliente con ID: " + id);
        }
    }

    // DELETE - Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente no encontrado con ID: " + id);
        }
    }
}