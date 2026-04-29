package com.gba.horizon.productapi.adapter.outbound.database;

import com.gba.horizon.productapi.adapter.exception.EntityNotFoundException;
import com.gba.horizon.productapi.adapter.outbound.database.entity.ProductEntity;
import com.gba.horizon.productapi.domain.Product;
import com.gba.horizon.productapi.usecase.ProductsCommandUseCase;
import com.gba.horizon.productapi.usecase.dto.CreatedProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductsCommandUseCaseImpl implements ProductsCommandUseCase {

    private final ProductsRepository productsRepository;

    public ProductsCommandUseCaseImpl(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    @Override
    public CreatedProduct createProduct(Product product) {
        final boolean exists = productsRepository.existsById(product.getSku());
        return new CreatedProduct(productsRepository.save(ProductEntity.fromProduct(product)), !exists);
    }

    @Override
    public void deleteProduct(String productId) {
        productsRepository.deleteById(productId);
    }

    @Override
    public Product updateProductDescription(String productId, String description) {
        final var productEntity = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + productId));
        productEntity.setDescription(description);
        return productsRepository.save(productEntity);
    }
}
