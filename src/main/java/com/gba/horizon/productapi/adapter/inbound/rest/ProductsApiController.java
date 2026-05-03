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

/**
 * ProductApiController maneja los request al ProductApi endpoints permitiendo: crear, recuperar, actualizar y eliminar
 * productos en nuestro API
 * @RestController -> permite crear un bean administrador por Spring
 * @RequestMapping("/api/products") -> Este controlador va a manejar las solicitudes que provengan de /api/products
 */

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

    /**
     * @PuMapping -> Creacion/actualizacion de la informacion de un producto
     * @ValidSku -> No se permite que dos productos tengan el mismo numero sku
     * Se retorna como respuesta 200 ok, 201 creado.
     */
    @PutMapping(value = "/{productId}")
    @Override
    public ResponseEntity<ProductOutput> createOrUpdateProduct(
            @PathVariable("productId")
            @ValidSku
            String productId,

            // enviamos productInput en el body del request
            @Valid
            @RequestBody
            ProductInput productInput) {
        final var product = productsCommandUseCase.createProduct(productInput.toProduct(productId));
        HttpStatus status = product.isNewProduct() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(productMapper.toProductOutput(product.product()));
    }

    @DeleteMapping(value = "/{productId}")
    @Override
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") String productId) {
        productsCommandUseCase.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductOutput> editProductDescription(String productId, ProductDescriptionInput input) {
        return null;
    }

    @GetMapping(value = "/{productId}")
    @Override
    public ResponseEntity<ProductOutput> getProductById(@PathVariable("productId") String productId) {
        final var product = productsQueryUseCase.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productMapper.toProductOutput(product));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ProductOutput>> getProducts() {
        final var products = productsQueryUseCase.getAllProducts()
                .stream()
                .map(productMapper::toProductOutput)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(products);
    }
}
