package com.gba.horizon.productapi.adapter.inbound.rest;

import com.gba.horizon.productapi.adapter.inbound.rest.configuration.ValidSku;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductDescriptionInput;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductInput;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductOutput;
import com.gba.horizon.productapi.adapter.inbound.rest.mapper.ProductMapper;
import com.gba.horizon.productapi.usecase.ProductsCommandUseCase;
import com.gba.horizon.productapi.usecase.ProductsQueryUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductsApiController implements ProductsApi{

    private final ProductsQueryUseCase productsQueryUseCase;
    private final ProductsCommandUseCase productsCommandUseCase;
    private final ProductMapper productMapper;

    public ProductsApiController(ProductsQueryUseCase productsQueryUseCase,
                                 ProductsCommandUseCase productsCommandUseCase,
                                 ProductMapper productMapper){
        this.productsQueryUseCase = productsQueryUseCase;
        this.productsCommandUseCase = productsCommandUseCase;
        this.productMapper = productMapper;
    }

    @PutMapping(value = "/{productId}")
    @Override
    public ResponseEntity<ProductOutput> createOrUpdateProduct(
            @PathVariable("productId")
            @ValidSku
            String productId,

            @Valid
            @RequestBody
            ProductInput productInput) {
        final var product = productsCommandUseCase.createProduct(productInput.toProduct(productId));
        HttpStatus status = product.isNewProduct() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(productMapper.toProductOutput(product.product()));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String productId) {
        return null;
    }

    @Override
    public ResponseEntity<ProductOutput> editProductDescription(String productId, ProductDescriptionInput input) {
        return null;
    }

    @Override
    public ResponseEntity<ProductOutput> getProductById(String productId) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProductOutput>> getProducts() {
        return null;
    }
}
