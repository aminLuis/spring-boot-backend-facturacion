package com.luis.facturacion.spring.boot.backend.apirest.services;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;

public interface IUploadsFile {

    public Resource cargar(String nombre) throws MalformedURLException;
}
