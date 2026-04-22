package com.gba.horizon.productapi.usecase;

import com.gba.horizon.productapi.domain.Product;
import com.gba.horizon.productapi.usecase.dto.CreatedProduct;

public interface ProductsCommandUseCase {

    CreatedProduct createProduct(Product product);
    void deleteProduct(String productId);
    Product updateProductDescription(String productId, String description);
}
