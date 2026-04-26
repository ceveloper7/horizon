package com.gba.horizon.productapi.adapter.inbound.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ProductDescriptionInput data transfer object permite modificar un producto sin afectar la clase de dominio
 */
public record ProductDescriptionInput(@NotBlank
                                      @Size(min = 10, max = 255)
                                      String description) {
}
