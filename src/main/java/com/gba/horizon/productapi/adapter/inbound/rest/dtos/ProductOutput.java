package com.gba.horizon.productapi.adapter.inbound.rest.dtos;

import java.math.BigDecimal;

/**
 * ProductOutput data transfer object permite modificar un producto sin afectar la clase de dominio
 */
public record ProductOutput(String name, String sku, String description, BigDecimal price) {
}
