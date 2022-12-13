package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.vitaliy_challenge.model.entities.SupplierWarehouse} entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SupplierWarehouseDto extends GenericDto implements Serializable
{
    private final String id;
    private final Set<ProductStockDto> productStocks;
    private final Set<ProductPurchasePriceListDto> productPurchasePriceLists;
}