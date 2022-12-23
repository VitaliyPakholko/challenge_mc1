package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductSalesPriceListDto;
import com.vitaliy_challenge.model.entities.ProductSalesPrice;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi",
        uses = {ProductMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductSalesMapper extends IMapper<ProductSalesPrice, ProductSalesPriceListDto>
{
    ProductSalesPrice toEntity(ProductSalesPrice productSalesPrice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSalesPrice partialUpdate(ProductSalesPriceListDto productSalesPriceListDto, @MappingTarget ProductSalesPrice productSalesPrice);

    ProductSalesPriceListDto toDto(ProductSalesPrice productSalesPrice);
}
