package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.SupplierWarehouseDto;
import com.vitaliy_challenge.model.entities.SupplierWarehouse;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi", uses = {ProductStockMapper.class, ProductPurchaseMapper.class})
public interface SupplierMapper extends IMapper<SupplierWarehouse, SupplierWarehouseDto>
{
    SupplierWarehouse toEntity(SupplierWarehouse supplierWarehouse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SupplierWarehouse partialUpdate(SupplierWarehouseDto supplierWarehouseDto, @MappingTarget SupplierWarehouse supplierWarehouse);

    SupplierWarehouseDto toDto(SupplierWarehouse supplierWarehouse);

    @AfterMapping
    default void linkProductStocks(@MappingTarget SupplierWarehouse supplierWarehouse)
    {
        supplierWarehouse.getProductStocks().forEach(productStock -> productStock.setSupplierWarehouseCode(supplierWarehouse));
    }

    @AfterMapping
    default void linkProductPurchasePriceLists(@MappingTarget SupplierWarehouse supplierWarehouse)
    {
        supplierWarehouse.getProductPurchasePriceLists().forEach(productPurchasePriceList -> productPurchasePriceList.setSupplierWarehouseCode(supplierWarehouse));
    }
}
