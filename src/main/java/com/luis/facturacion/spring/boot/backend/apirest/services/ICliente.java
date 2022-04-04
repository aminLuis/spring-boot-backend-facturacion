package com.luis.facturacion.spring.boot.backend.apirest.services;

import java.util.List;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICliente {

    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public Cliente findById(Integer id);

    public Cliente save(Cliente nuevo);

    public void delete(Integer id);
}
