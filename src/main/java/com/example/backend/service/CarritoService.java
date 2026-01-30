package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioRepository usuarioRepo;
    private final CarritoItemRepository itemRepo;

    public CarritoService(
            CarritoRepository carritoRepo,
            ProductoRepository productoRepo,
            UsuarioRepository usuarioRepo,
            CarritoItemRepository itemRepo
    ) {
        this.carritoRepo = carritoRepo;
        this.productoRepo = productoRepo;
        this.usuarioRepo = usuarioRepo;
        this.itemRepo = itemRepo;
    }

    // Este método arregla la línea roja en tu CarritoController
    public Carrito obtenerPorUsuario(Long usuarioId) {
        return carritoRepo.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepo.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no existe"));
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepo.save(nuevoCarrito);
                });
    }

    // Este método arregla la línea roja en tu CompraController
    public double calcularTotal(Long usuarioId) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        return carrito.getItems().stream()
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();
    }

    @Transactional
    public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        Carrito carrito = obtenerPorUsuario(usuarioId);
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        for (CarritoItem item : carrito.getItems()) {
            if (item.getProducto().getId().equals(productoId)) {
                item.setCantidad(item.getCantidad() + cantidad);
                return carritoRepo.save(carrito);
            }
        }

        CarritoItem nuevoItem = new CarritoItem(producto, cantidad, carrito);
        carrito.getItems().add(nuevoItem);
        return carritoRepo.save(carrito);
    }

    @Transactional
    public void sumar(Long itemId) {
        CarritoItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        item.setCantidad(item.getCantidad() + 1);
        itemRepo.save(item);
    }

    @Transactional
    public void restar(Long itemId) {
        CarritoItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        if (item.getCantidad() <= 1) {
            itemRepo.delete(item);
        } else {
            item.setCantidad(item.getCantidad() - 1);
            itemRepo.save(item);
        }
    }

    @Transactional
    public void eliminar(Long itemId) {
        itemRepo.deleteById(itemId);
    }
}