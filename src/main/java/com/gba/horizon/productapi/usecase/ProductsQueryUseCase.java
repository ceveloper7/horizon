package com.gba.horizon.productapi.usecase;

import com.gba.horizon.productapi.domain.Product;

import java.util.List;

/**
 * Caso de uso del negocio, que permite obtener un product y una lista de productos
 */
public interface ProductsQueryUseCase {

    Product getProductById(String productId);
    List<? extends Product> getAllProducts();
}
