package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductPurchasePriceListDto;
import com.vitaliy_challenge.model.entities.ProductPurchasePrice;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi",
        uses = {ProductMapper.class, SupplierMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductPurchaseMapper extends IMapper<ProductPurchasePrice, ProductPurchasePriceListDto>
{
    ProductPurchasePrice toEntity(ProductPurchasePrice productPurchasePrice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductPurchasePrice partialUpdate(ProductPurchasePriceListDto productPurchasePriceListDto, @MappingTarget ProductPurchasePrice productPurchasePrice);

    ProductPurchasePriceListDto toDto(ProductPurchasePrice productPurchasePrice);
}
