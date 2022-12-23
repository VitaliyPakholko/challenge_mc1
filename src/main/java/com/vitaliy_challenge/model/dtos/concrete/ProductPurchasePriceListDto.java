package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import com.vitaliy_challenge.model.entities.ProductPurchasePrice;
import com.vitaliy_challenge.model.entities.SupplierWarehouse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * A DTO for the {@link ProductPurchasePrice} entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductPurchasePriceListDto extends GenericDto implements Serializable
{
    private final Long id;
    private final Double price;
    private final ProductDtoFull productSku;
    private final SupplierWarehouse supplierWarehouseCode;
}