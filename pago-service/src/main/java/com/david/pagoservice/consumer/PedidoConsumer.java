package com.david.pagoservice.consumer;

import com.david.pagoservice.model.PedidoPagoDTO;
import com.david.pagoservice.model.ResultadoPagoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "pedidos", groupId = "pago-group")
    public void escucharPedido(String pedidoJson) {
        try {
            PedidoPagoDTO pedido = objectMapper.readValue(pedidoJson, PedidoPagoDTO.class);

            String estadoPago;
            if (pedido.precioTotal() > 100.0) {
                estadoPago = "RECHAZADO";
                System.out.println("Pago denegado: El importe supera los 100€");
            } else {
                estadoPago = "ACEPTADO";
                System.out.println("Pago aprobado para el pedido: " + pedido.idPedido());
            }

            ResultadoPagoDTO resultado = new ResultadoPagoDTO(pedido.idPedido(), estadoPago);

            kafkaTemplate.send("pagos", pedido.idPedido(), objectMapper.writeValueAsString(resultado));
            System.out.println("Resultado del pago enviado Kafka para el pedido: " + pedido.idPedido());
        } catch (Exception e) {
            System.out.println("Error al enviar el pedido: " + pedidoJson);
        }
    }
}
