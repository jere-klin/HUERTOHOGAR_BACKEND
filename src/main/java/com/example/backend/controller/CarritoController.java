package com.example.backend.controller;

import com.example.backend.model.Carrito;
import com.example.backend.service.CarritoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    // VER CARRITO POR USUARIO (Nuevo método)
    @GetMapping("/usuario/{usuarioId}")
    public Carrito obtenerPorUsuario(@PathVariable Long usuarioId) {
        return service.obtenerPorUsuario(usuarioId);
    }

    // Agregar producto
    @PostMapping("/{usuarioId}/agregar/{productoId}")
    public Carrito agregar(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam int cantidad
    ) {
        return service.agregarProducto(usuarioId, productoId, cantidad);
    }

    // SUMAR
    @PutMapping("/item/{itemId}/sumar")
    public void sumar(@PathVariable Long itemId) {
        service.sumar(itemId);
    }

    // RESTAR
    @PutMapping("/item/{itemId}/restar")
    public void restar(@PathVariable Long itemId) {
        service.restar(itemId);
    }

    // ELIMINAR
    @DeleteMapping("/item/{itemId}")
    public void eliminar(@PathVariable Long itemId) {
        service.eliminar(itemId);
    }
}
