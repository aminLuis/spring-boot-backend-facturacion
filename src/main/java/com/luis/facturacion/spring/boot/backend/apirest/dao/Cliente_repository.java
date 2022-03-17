package com.luis.facturacion.spring.boot.backend.apirest.dao;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;

import org.springframework.data.repository.CrudRepository;

public interface Cliente_repository extends CrudRepository<Cliente, Integer> {

}
