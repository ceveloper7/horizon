package com.gba.horizon.productapi.adapter.inbound.rest;

import com.gba.horizon.productapi.adapter.inbound.rest.configuration.ValidSku;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductDescriptionInput;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductInput;
import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductOutput;
import com.gba.horizon.productapi.adapter.inbound.rest.mapper.ProductMapper;
import com.gba.horizon.productapi.usecase.ProductsCommandUseCase;
import com.gba.horizon.productapi.usecase.ProductsQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

    @Operation(
            operationId = "deleteProduct",
            summary = "Logical remove a product by ID",
            responses = {
                    @ApiResponse(responseCode = "204",
                    description = "Product removed successfully"),
            }
    )
    @DeleteMapping(value = "/{productId}")
    @Override
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") @ValidSku String productId) {
        productsCommandUseCase.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{productId}")
    @Override
    public ResponseEntity<ProductOutput> editProductDescription(
            @PathVariable("productId") @ValidSku String productId,
            @RequestBody @Valid ProductDescriptionInput input) {
        final var product = productsCommandUseCase.updateProductDescription(productId, input.description());
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toProductOutput(product));
    }

    @Operation(
            operationId = "getProductById",
            summary = "Retrieve a product by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found",
                        content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductOutput.class))}),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                        content = {
                            @Content(schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(name = "Validation Error",
                                        summary = "Example of validation error",
                                        value = """
                                        {"type":"about:blank",
                                            "title":"Not found",
                                            "status":"404",
                                            "detail":"Product not found with id AK21102",
                                            "instance":"/api/products/AK21102"
                                        }
                                        """
                                    )
                            })
                    })
            }
    )
    @GetMapping(value = "/{productId}")
    @Override
    public ResponseEntity<ProductOutput> getProductById(@PathVariable("productId") @ValidSku String productId) {
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
