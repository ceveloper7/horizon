package com.gba.horizon.productapi.adapter.inbound.rest;

import com.gba.horizon.productapi.adapter.inbound.rest.configuration.ValidSku;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductDescriptionInput;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductInput;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductOutput;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Detallamos todos los endpoints para la entidad Product.
 */
public interface ProductsApi {

    ResponseEntity<ProductOutput> createOrUpdateProduct(
            @ValidSku String productId,
            @Valid ProductInput productInput);

    ResponseEntity<Void> deleteProduct(@ValidSku String productId);

    ResponseEntity<ProductOutput> editProductDescription(
            @ValidSku String productId,
            @Valid ProductDescriptionInput input);

    ResponseEntity<ProductOutput> getProductById(@ValidSku String productId);

    ResponseEntity<List<ProductOutput>> getProducts();
}
