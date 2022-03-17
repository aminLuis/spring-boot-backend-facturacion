package com.luis.facturacion.spring.boot.backend.apirest.services;

import java.util.List;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;

public interface ICliente {

    public List<Cliente> findAll();
}
