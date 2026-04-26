package com.gba.horizon.productapi.adapter.inbound.rest.mapper;

import com.gba.horizon.productapi.adapter.inbound.rest.dtos.ProductOutput;
import com.gba.horizon.productapi.domain.Product;
import org.mapstruct.Mapper;

/**
 * MapStruct permite transformar un objeto en otro objeto
 * Mapeamos los datos del objeto de dominio Product y lo convertimos en un DTO ProductOutput
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductOutput toProductOutput(Product product);
}
