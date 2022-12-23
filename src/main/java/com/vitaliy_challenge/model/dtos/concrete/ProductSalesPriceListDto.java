package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import com.vitaliy_challenge.model.entities.ProductSalesPrice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link ProductSalesPrice} entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductSalesPriceListDto extends GenericDto implements Serializable
{
    private final Long id;
    private final ProductDtoFull productSku;
    private final Double streetPriceVat;
    private final Double finalPrice;
    private final String promotion;
    private final Instant dateFrom;
    private final Instant dateTo;
    private final Boolean isActive;
}