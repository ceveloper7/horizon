package com.gba.horizon.productapi.usecase.dto;

import com.gba.horizon.productapi.domain.Product;

public record CreatedProduct(Product product, boolean isNewProduct) {
}
