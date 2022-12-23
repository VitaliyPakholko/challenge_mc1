package com.vitaliy_challenge.controller.salesPricesLogic;

public interface SalesPricesGenerator
{
    public void deleteAllPricings();

    public void generateAllPricings() throws RuntimeException;

    public Long countGeneratedPricings();
}
