package com.david.envioservice.consumer;

import com.david.envioservice.model.PagoEventoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagoConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "pagos", groupId = "envio-group")
    public void procesarEnvio(String pagoJson) {
        try {
            PagoEventoDTO pago = objectMapper.readValue(pagoJson, PagoEventoDTO.class);

            if ("ACEPTADO".equals(pago.estadoPago())) {
                System.out.println("¡Pago confirmado! Preparando el paquete para el pedido: " + pago.idPedido());
                System.out.println("El paquete ha sido enviado a logística.");
            } else {
                System.out.println("Pedido " + pago.idPedido() + " descartado. El pago fue rechazado.");
            }

        } catch (Exception e) {
            System.err.println("Error al procesar el evento de envío: " + e.getMessage());
        }
    }
}