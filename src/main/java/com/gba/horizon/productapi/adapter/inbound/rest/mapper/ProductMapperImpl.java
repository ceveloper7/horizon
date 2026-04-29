package com.gba.horizon.productapi.adapter.inbound.rest.mapper;

import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductOutput;
import com.gba.horizon.productapi.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper{

    @Override
    public ProductOutput toProductOutput(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductOutput(
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
