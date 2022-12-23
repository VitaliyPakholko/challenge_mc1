package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import com.vitaliy_challenge.model.entities.Category;
import com.vitaliy_challenge.model.entities.ProductPurchasePriceList;
import com.vitaliy_challenge.model.entities.ProductSalesPrice;
import com.vitaliy_challenge.model.entities.ProductStock;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.vitaliy_challenge.model.entities.Product} entity
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProductDtoFull extends GenericDto implements Serializable
{
    private final String id;
    private final String mpn;
    private final Double streetPriceVat;
    private final Double vatValue;
    private final Category categoryCode;
    private final Set<ProductStock> productStocks;
    private final Set<ProductPurchasePriceList> productPurchasePriceLists;
    private final Set<ProductSalesPrice> productSalesPrices;
}