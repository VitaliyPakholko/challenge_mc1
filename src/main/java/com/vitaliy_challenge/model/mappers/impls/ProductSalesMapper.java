package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductSalesPriceListDto;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductSalesPriceList;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "cdi", uses = {ProductMapper.class})
public interface ProductSalesMapper extends IMapper<ProductSalesPriceList, ProductSalesPriceListDto>
{
    ProductSalesPriceList toEntity(ProductSalesPriceList productSalesPriceList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSalesPriceList partialUpdate(ProductSalesPriceListDto productSalesPriceListDto, @MappingTarget ProductSalesPriceList productSalesPriceList);

    ProductSalesPriceListDto toDto(ProductSalesPriceList productSalesPriceList);
}
