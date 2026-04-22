package com.gba.horizon.productapi.adapter.inbound.rest.dtos;

import java.math.BigDecimal;

public record ProductOutput(String name, String sku, String description, BigDecimal price) {
}
