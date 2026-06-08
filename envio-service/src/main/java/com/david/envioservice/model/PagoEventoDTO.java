package com.david.envioservice.model;

public record PagoEventoDTO(
        String idPedido,
        String estadoPago
) {}