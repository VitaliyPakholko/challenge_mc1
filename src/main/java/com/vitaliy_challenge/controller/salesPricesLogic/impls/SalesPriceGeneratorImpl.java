package com.vitaliy_challenge.controller.salesPricesLogic.impls;

import com.vitaliy_challenge.controller.rest.repositories.ProductPurchasePriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductSalesPriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductStockRepository;
import com.vitaliy_challenge.controller.salesPricesLogic.SalesPricesGenerator;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.Constants;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.CustomMarginCategoriesEnum;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductPurchasePriceList;
import com.vitaliy_challenge.model.entities.ProductStock;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@ApplicationScoped
public class SalesPriceGeneratorImpl implements SalesPricesGenerator
{
    @Inject
    ProductPurchasePriceRepository purchaseRepository;

    @Inject
    ProductSalesPriceRepository salesRepository;

    @Inject
    ProductStockRepository stockRepository;

    private  Map<ProductMapKey, ProductPriceInfo> productPriceMap;

    @AllArgsConstructor
    private class ProductMapKey
    {
        private String sku;
        private String warehouse;
    }

    @Override
    @PostConstruct
    @Transactional
    public void deleteAllPricings()
    {
        return;
//        salesRepository.deleteAll();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPriceInfo
    {
        private String sku;
        private Double purchasePrice;
        private Double purchasePriceVat;
        private Double streetPriceVat;
        private Double vatValue;
        private String category;
        private Double generatedPrice;
//        private Boolean hasX_M1;
        private Boolean hasX_M2orM4;
        private Boolean hasM1AndM2;
        private Boolean noWarehousePreference;
        private String warehouse;
        private String bestWarehouse;

    }

    @Override
    public void generateAllPricings()
    {

       this.populatePurchasePriceMap();
       this.populateMapPrices();

    }

    private void populatePurchasePriceMap()
    {
        List<ProductPurchasePriceList> purchasePriceList = purchaseRepository.listAll();
        this.productPriceMap = new HashMap<>();
        purchasePriceList.forEach(
                p -> productPriceMap.put(
                        new ProductMapKey(p.getProductCode(), p.getSupplierWarehouseCode().getId()),
                        ProductPriceInfo.builder()
                                .sku(           p.getProductCode())
                                .purchasePrice( p.getPrice())
                                .streetPriceVat(p.getProductSku().getStreetPriceVat())
                                .vatValue(      p.getProductSku().getVatValue())
                                .category(      p.getProductSku().getCategoryCodeString())
                                .warehouse(     p.getSupplierWarehouseCode().getId())
                                .build()

                ));
    }

    private void populateMapPrices()
    {
        for(ProductPriceInfo p : this.productPriceMap.values())
        {
            double vatGeneratedPrice = p.streetPriceVat * CustomMarginCategoriesEnum.streetMarginByCategory(p.category);
            double vatlessGeneratedPrice = vatGeneratedPrice * (1D - p.vatValue);
            log.info("VatlessGeneratedPrice for product " + p.sku + " is: " + vatlessGeneratedPrice);

            if(vatlessGeneratedPrice > p.purchasePrice)
            {
                p.generatedPrice = vatlessGeneratedPrice;
                log.info("VatlessGeneratedPrice is greater than purchase price: " + p.purchasePrice);
            }
            else
            {
                double vatlessStreetPrice = p.streetPriceVat * (1D - p.vatValue);
                log.info("VatlessGeneratedPrice is lesser than purchase price: " + p.purchasePrice);
                log.info("VatlessStreetPrice for product " + p.sku + " is: " + vatlessStreetPrice);
                p.generatedPrice = Math.min(p.purchasePrice * Constants.DEFAULT_PRICE_INCREMENT, vatlessStreetPrice);
                log.info("VatlessGeneratedPrice for product " + p.sku + " is: " + p.generatedPrice +
                         ". Min(" + p.purchasePrice * Constants.DEFAULT_PRICE_INCREMENT + ", " + vatlessStreetPrice +")");
            }


        }
    }

    private void selectProductWarehouse(ProductPriceInfo productInfo)
    {
        List<ProductStock> stocks = stockRepository.findQuantityAndWarehouseByProductCode(productInfo.sku);
        if(stocks.stream().filter(s -> s.getQuantity() > Constants.MINIMUM_WAREHOUSE_STOCK).anyMatch(s -> s.getWarehouseString().equals("M1")))
        {
            productInfo.bestWarehouse = "M1";
            return;
        }

        List<ProductStock> numerousFilteredStocks = stocks.stream()
                .filter(s -> s.getQuantity() > Constants.MINIMUM_WAREHOUSE_STOCK)
                .filter(s -> ((s.getWarehouseString().equals("M2")))||(s.getWarehouseString().equals("M4")))
                .collect(Collectors.toList());
        if(!numerousFilteredStocks.isEmpty())
        {
            if (numerousFilteredStocks.size() == 1)
            {
                productInfo.bestWarehouse = numerousFilteredStocks.get(0).getWarehouseString();
            }
            else
            {
                ProductPriceInfo m2Info = productPriceMap.get(new ProductMapKey(productInfo.sku, "M2"));
                double m2Margin = m2Info.generatedPrice - m2Info.purchasePrice;
                ProductPriceInfo m4Info = productPriceMap.get(new ProductMapKey(productInfo.sku, "M4"));
                double m4Margin = m4Info.generatedPrice - m4Info.purchasePrice;

                if(m2Margin > m4Margin)
                    productInfo.bestWarehouse = "M2";
                else
                    productInfo.bestWarehouse = "M4";
            }
            return;
        }

//        else

//        if(stocks.stream().filter(s -> s.getQuantity() > Constants.MINIMUM_WAREHOUSE_STOCK)
//                .anyMatch(s -> ((s.getWarehouseString().equals("M2")))||(s.getWarehouseString().equals("M4"))))
//        {
//            productInfo.hasX_M2orM4 = true;
//            return;
//        }

    }

    private List<ProductPurchasePriceList> createPurchasePriceEntities()
    {
        return null;
    }

    @Override
    public Long countGeneratedPricings()
    {
        return null;
    }
}
