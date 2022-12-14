package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductDto;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi", uses = {CategoryMapper.class,
        ProductStockMapper.class, ProductPurchaseMapper.class, ProductSalesMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductMapper extends IMapper<Product, ProductDto>
{
    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductDto productDto, @MappingTarget Product product);

    @AfterMapping
    default void linkProductStocks(@MappingTarget Product product)
    {
        product.getProductStocks().forEach(productStock -> productStock.setProductSku(product));
    }

    @AfterMapping
    default void linkProductPurchasePriceLists(@MappingTarget Product product)
    {
        product.getProductPurchasePriceLists().forEach(productPurchasePriceList -> productPurchasePriceList.setProductSku(product));
    }

    @AfterMapping
    default void linkProductSalesPriceLists(@MappingTarget Product product)
    {
        product.getProductSalesPriceLists().forEach(productSalesPriceList -> productSalesPriceList.setProductSku(product));
    }
}
