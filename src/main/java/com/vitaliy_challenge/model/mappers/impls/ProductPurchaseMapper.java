package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductPurchasePriceListDto;
import com.vitaliy_challenge.model.entities.ProductPurchasePriceList;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi",
        uses = {ProductMapper.class, SupplierMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductPurchaseMapper extends IMapper<ProductPurchasePriceList, ProductPurchasePriceListDto>
{
    ProductPurchasePriceList toEntity(ProductPurchasePriceList productPurchasePriceList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductPurchasePriceList partialUpdate(ProductPurchasePriceListDto productPurchasePriceListDto, @MappingTarget ProductPurchasePriceList productPurchasePriceList);

    ProductPurchasePriceListDto toDto(ProductPurchasePriceList productPurchasePriceList);
}
