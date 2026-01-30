package com.example.backend.controller;

import com.example.backend.model.Compra;
import com.example.backend.service.CarritoService;
import com.example.backend.service.CompraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compra")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;
    private final CarritoService carritoService;

    public CompraController(CompraService compraService,
                             CarritoService carritoService) {
        this.compraService = compraService;
        this.carritoService = carritoService;
    }

    @PostMapping("/{usuarioId}")
    public Compra confirmar(@PathVariable Long usuarioId) {
        double total = carritoService.calcularTotal(usuarioId);
        return compraService.confirmarCompra(usuarioId, total);
    }
}
