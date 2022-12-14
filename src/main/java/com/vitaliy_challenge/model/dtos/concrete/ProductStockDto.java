package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import com.vitaliy_challenge.model.entities.SupplierWarehouse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * A DTO for the {@link com.vitaliy_challenge.model.entities.ProductStock} entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductStockDto extends GenericDto implements Serializable
{
    private final Long id;
    private final ProductDtoFull productSku;
    private final Integer quantity;
    private final SupplierWarehouse supplierWarehouseCode;
}