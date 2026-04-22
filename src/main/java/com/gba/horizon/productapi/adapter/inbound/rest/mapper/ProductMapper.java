package com.gba.horizon.productapi.adapter.inbound.rest.mapper;

import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductOutput;
import com.gba.horizon.productapi.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductOutput toProductOutput(Product product);
}
