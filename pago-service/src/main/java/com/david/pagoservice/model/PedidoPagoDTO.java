package com.david.pagoservice.model;

public record PedidoPagoDTO(
        String idPedido,
        Double precioTotal
) {}