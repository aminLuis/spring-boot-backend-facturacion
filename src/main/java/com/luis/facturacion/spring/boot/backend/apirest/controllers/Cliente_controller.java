package com.luis.facturacion.spring.boot.backend.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.luis.facturacion.spring.boot.backend.apirest.models.Cliente;
import com.luis.facturacion.spring.boot.backend.apirest.services.ICliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> getCliente(@PathVariable Integer id) {
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            cliente = servicio.findById(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null) {
            response.put("Mensaje", "El cliente " + id + " no fue encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping("/cliente")
    public ResponseEntity<?> save(@Valid @RequestBody Cliente nuevo, BindingResult result) {
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(err -> {
                return "El campo " + err.getField() + " " + err.getDefaultMessage();
            }).collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            cliente = servicio.save(nuevo);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al registrar en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @PutMapping("/cliente/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Integer id, @RequestBody Cliente data, BindingResult result) {

        Cliente actual = servicio.findById(id);
        Map<String, Object> response = new HashMap<>();

        if (actual == null) {
            response.put("Mensaje", "El cliente " + id + " no fue encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(err -> {
                return "El campo " + err.getField() + " " + err.getDefaultMessage();
            }).collect(Collectors.toList());

            response.put("Error", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            actual.setNombre(data.getNombre());
            actual.setApellidos(data.getApellidos());
            actual.setEmail(data.getEmail());
            servicio.save(actual);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al buscar cliente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actual, HttpStatus.CREATED);
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        try {

            Cliente cliente = servicio.findById(id);
            String nameAnterior = cliente.getFoto();

            if (nameAnterior != null && nameAnterior.length() > 0) {
                Path routeAnterior = Paths.get("uploads").resolve(nameAnterior).toAbsolutePath();
                File fileAnterior = routeAnterior.toFile();
                if (fileAnterior.exists() && fileAnterior.canRead()) {
                    fileAnterior.delete();
                }
            }

            servicio.delete(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al eliminar cliente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/cliente/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        Cliente cliente = servicio.findById(id);

        if (!file.isEmpty()) {
            String name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
            Path routeFile = Paths.get("uploads").resolve(name).toAbsolutePath();

            try {
                Files.copy(file.getInputStream(), routeFile);
            } catch (IOException e) {
                response.put("mensaje", "Error al subir la imagen");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String nameAnterior = cliente.getFoto();

            if (nameAnterior != null && nameAnterior.length() > 0) {
                Path routeAnterior = Paths.get("uploads").resolve(nameAnterior).toAbsolutePath();
                File fileAnterior = routeAnterior.toFile();
                if (fileAnterior.exists() && fileAnterior.canRead()) {
                    fileAnterior.delete();
                }
            }

            cliente.setFoto(name);
            servicio.save(cliente);

            response.put("cliente", cliente);
            response.put("Mensaje", "Se ha subido correctamente la imagen " + name);

        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/img/{fileName:.+}")
    public ResponseEntity<Resource> ver_foto(@PathVariable String fileName) {

        Path routeFile = Paths.get("uploads").resolve(fileName).toAbsolutePath();

        Resource recurso = null;

        try {
            recurso = new UrlResource(routeFile.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (!recurso.exists() && !recurso.isReadable()) {
            routeFile = Paths.get("src/main/resources/static/images").resolve("user.png").toAbsolutePath();

            try {
                recurso = new UrlResource(routeFile.toUri());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // throw new RuntimeException("Error: No se pudo cargar la imagen " + fileName);

        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + recurso.getFilename() + "\"");

        return new ResponseEntity<Resource>(recurso, HttpStatus.OK);
    }

}
