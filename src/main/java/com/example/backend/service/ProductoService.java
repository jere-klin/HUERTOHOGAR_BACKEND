package com.example.backend.service;

import com.example.backend.model.Producto;
import com.example.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public Producto actualizar(Long id, Producto productoDetalles) {
        Producto existente = obtenerPorId(id);
        
        existente.setNombre(productoDetalles.getNombre());
        existente.setDescripcion(productoDetalles.getDescripcion());
        existente.setPrecio(productoDetalles.getPrecio());
        existente.setStock(productoDetalles.getStock());
        
        // Importante: Actualizamos el nombre de la imagen
        existente.setImg(productoDetalles.getImg()); 
        
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}