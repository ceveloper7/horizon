package com.gba.horizon.productapi.adapter.inbound.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductDescriptionInput(@NotBlank
                                      @Size(min = 10, max = 255)
                                      String description) {
}
