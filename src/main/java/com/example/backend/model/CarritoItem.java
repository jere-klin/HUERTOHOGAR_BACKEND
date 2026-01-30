package com.example.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference; // Importante añadir el import

@Entity
@Table(name = "carrito_item")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    @JsonBackReference // <-- CAMBIO AQUÍ: Esto evita que se repita el carrito infinitamente
    private Carrito carrito;

    // ... (El resto de tus constructores y getters se quedan igual)
    
    public CarritoItem() {}

    public CarritoItem(Producto producto, int cantidad, Carrito carrito) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.carrito = carrito;
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; } // No olvides el setter
}