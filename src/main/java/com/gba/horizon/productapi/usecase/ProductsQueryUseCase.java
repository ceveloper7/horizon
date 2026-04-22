package com.gba.horizon.productapi.usecase;

import com.gba.horizon.productapi.domain.Product;

import java.util.List;

public interface ProductsQueryUseCase {

    Product getProductById(String productId);
    List<? extends Product> getAllProducts();
}
