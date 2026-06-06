package com.david.pedidoservice.model;

public record Pedido (
        String idPedido,
        String nombreProducto,
        Integer cantidad,
        Double precioTotal
){}
