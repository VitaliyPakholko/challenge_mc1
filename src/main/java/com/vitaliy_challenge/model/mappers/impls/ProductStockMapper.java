package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductStockDto;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductStock;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductStockMapper extends IMapper<ProductStock, ProductStockDto>
{
    ProductStock toEntity(ProductStock productStock);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductStock partialUpdate(ProductStockDto productStockDto, @MappingTarget ProductStock productStock);

    ProductStockDto toDto(ProductStock productStock);
}
