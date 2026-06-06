package com.david.pedidoservice.controller;

import com.david.pedidoservice.model.Pedido;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public String procesarCompra(@RequestBody Pedido nuevoPedido) {
        try {
            String pedidoJson = objectMapper.writeValueAsString(nuevoPedido);

            kafkaTemplate.send("pedidos", nuevoPedido.idPedido(), pedidoJson);

            System.out.println("Pedido enviado con Kafka");

            return "El pedido " + nuevoPedido.idPedido() + " esta siendo procesado";
        } catch (Exception e) {
            return "Error al procesar el pedido " + e.getMessage();
        }
    }
}
