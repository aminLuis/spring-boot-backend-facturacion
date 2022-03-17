package com.luis.facturacion.spring.boot.backend.apirest.services;

import java.util.List;

import com.luis.facturacion.spring.boot.backend.apirest.dao.Cliente_repository;
import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cliente_service implements ICliente {

    @Autowired
    private Cliente_repository repositorio;

    @Override
    public List<Cliente> findAll() {

        return (List<Cliente>) repositorio.findAll();
    }

}
