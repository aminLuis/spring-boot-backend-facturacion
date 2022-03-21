package com.luis.facturacion.spring.boot.backend.apirest.controllers;

import java.util.List;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;
import com.luis.facturacion.spring.boot.backend.apirest.services.ICliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/cliente/{id}")
    public Cliente getCliente(@PathVariable("id") Integer id) {
        return servicio.findById(id);
    }

    @PostMapping("/cliente")
    public Cliente save(@RequestBody Cliente nuevo) {
        return servicio.save(nuevo);
    }

    @DeleteMapping("/cliente/{id}")
    public void delete(@PathVariable("id") Integer id) {
        servicio.delete(id);
    }
}
