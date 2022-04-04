package com.luis.facturacion.spring.boot.backend.apirest.dao;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Cliente_repository extends JpaRepository<Cliente, Integer> {

}
