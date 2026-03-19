package com.tienda.victoria.service;

import com.tienda.victoria.dto.ClienteDTO;
import com.tienda.victoria.model.Cliente;
import com.tienda.victoria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    // Listar todos los clientes
    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar cliente por ID
    public ClienteDTO buscarPorId(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            return convertirADTO(cliente.get());
        }
        throw new RuntimeException("Cliente no encontrado con ID: " + id);
    }

    // Guardar nuevo cliente
    public ClienteDTO guardarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertirAEntidad(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirADTO(clienteGuardado);
    }

    // Actualizar cliente
    public ClienteDTO actualizarCliente(Integer id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNombreCliente(clienteDTO.getNombreCliente());
            cliente.setApellidoCliente(clienteDTO.getApellidoCliente());
            cliente.setTipoDocumento(clienteDTO.getTipoDocumento());
            cliente.setNumeroDocumento(clienteDTO.getNumeroDocumento());
            cliente.setTelefonoCliente(clienteDTO.getTelefonoCliente());
            cliente.setCorreoCliente(clienteDTO.getCorreoCliente());
            Cliente clienteActualizado = clienteRepository.save(cliente);
            return convertirADTO(clienteActualizado);
        }
        throw new RuntimeException("Cliente no encontrado con ID: " + id);
    }

    // Eliminar cliente
    public void eliminarCliente(Integer id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    // Convertir Entidad → DTO
    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombreCliente(cliente.getNombreCliente());
        dto.setApellidoCliente(cliente.getApellidoCliente());
        dto.setTipoDocumento(cliente.getTipoDocumento());
        dto.setNumeroDocumento(cliente.getNumeroDocumento());
        dto.setTelefonoCliente(cliente.getTelefonoCliente());
        dto.setCorreoCliente(cliente.getCorreoCliente());
        return dto;
    }

    // Convertir DTO → Entidad
    private Cliente convertirAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(dto.getIdCliente());
        cliente.setNombreCliente(dto.getNombreCliente());
        cliente.setApellidoCliente(dto.getApellidoCliente());
        cliente.setTipoDocumento(dto.getTipoDocumento());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setTelefonoCliente(dto.getTelefonoCliente());
        cliente.setCorreoCliente(dto.getCorreoCliente());
        return cliente;
    }
}
