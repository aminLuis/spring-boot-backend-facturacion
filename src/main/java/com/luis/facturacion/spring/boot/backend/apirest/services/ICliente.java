package com.luis.facturacion.spring.boot.backend.apirest.services;

import java.util.List;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;

public interface ICliente {

    public List<Cliente> findAll();

    public Cliente findById(Integer id);

    public Cliente save(Cliente nuevo);

    public Cliente update(Integer id, Cliente data);

    public void delete(Integer id);
}
