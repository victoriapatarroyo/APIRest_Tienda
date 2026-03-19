package com.tienda.victoria.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Integer idCliente;

    @Column(name = "nombreCliente", nullable = false, length = 50)
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

    // Constructor vacío
    public Cliente() {}

    // Constructor completo
    public Cliente(Integer idCliente, String nombreCliente, String apellidoCliente,
                   String tipoDocumento, String numeroDocumento,
                   String telefonoCliente, String correoCliente) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
    }

    // Getters y Setters
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
}