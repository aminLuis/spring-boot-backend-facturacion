package com.luis.facturacion.spring.boot.backend.apirest.controllers;

import java.util.List;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;
import com.luis.facturacion.spring.boot.backend.apirest.services.ICliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Cliente_controller {

    @Autowired
    private ICliente servicio;

    @GetMapping("/cliente")
    public List<Cliente> getClientes() {
        return servicio.findAll();
    }
}
