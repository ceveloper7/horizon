package com.gba.horizon.productapi.adapter.outbound.database;

import com.gba.horizon.productapi.adapter.exception.EntityNotFoundException;
import com.gba.horizon.productapi.domain.Product;
import com.gba.horizon.productapi.usecase.ProductsQueryUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsQueryUseCaseImpl implements ProductsQueryUseCase {

    private final ProductsRepository productsRepository;

    public ProductsQueryUseCaseImpl(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    @Override
    public Product getProductById(String productId) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + productId));
    }

    @Override
    public List<? extends Product> getAllProducts() {
        return productsRepository.findAll();
    }
}
