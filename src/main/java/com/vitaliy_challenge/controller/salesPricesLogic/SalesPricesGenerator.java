package com.vitaliy_challenge.controller.salesPricesLogic;

import com.vitaliy_challenge.model.entities.ProductSalesPrice;

import java.util.List;

public interface SalesPricesGenerator
{
    public void deleteAllPricings();

    public List<ProductSalesPrice>  generateAllPricings() throws RuntimeException;

    public void persistGeneratedPriceEntities(List<ProductSalesPrice> p);
}
