package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante

@Service
public class CompraService {

    private final CompraRepository compraRepo;
    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo; // Necesario para el stock

    public CompraService(CompraRepository compraRepo,
                         CarritoRepository carritoRepo,
                         ProductoRepository productoRepo) {
        this.compraRepo = compraRepo;
        this.carritoRepo = carritoRepo;
        this.productoRepo = productoRepo;
    }

    @Transactional // Si falla el guardado de la compra, el carrito NO se vacía
    public Compra confirmarCompra(Long usuarioId, double total) {
        Carrito carrito = carritoRepo.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("No puedes comprar con un carrito vacío");
        }

        // 1. DESCONTAR STOCK de cada producto
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock para: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepo.save(producto); // Actualizamos el stock en la BD
        }

        // 2. CREAR LA COMPRA
        Compra compra = new Compra(carrito.getUsuario(), total);
        Compra compraGuardada = compraRepo.save(compra);

        // 3. VACIAR CARRITO
        carrito.getItems().clear(); 
        carritoRepo.save(carrito);

        return compraGuardada;
    }
}
