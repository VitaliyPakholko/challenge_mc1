package com.vitaliy_challenge.controller.salesPricesLogic.impls;

import com.vitaliy_challenge.controller.rest.repositories.ProductPurchasePriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductSalesPriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductStockRepository;
import com.vitaliy_challenge.controller.salesPricesLogic.SalesPricesGenerator;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.Constants;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.CustomMarginCategoriesEnum;
import com.vitaliy_challenge.model.entities.ProductPurchasePriceList;
import com.vitaliy_challenge.model.entities.ProductSalesPriceList;
import com.vitaliy_challenge.model.entities.ProductStock;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Log4j2
@ApplicationScoped
public class SalesPriceGeneratorImpl implements SalesPricesGenerator
{
    @Inject
    ProductPurchasePriceRepository purchaseRepository;

    @Inject
    ProductSalesPriceRepository salesRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    ProductStockRepository stockRepository;

    private  Map<String, Map<String,ProductPriceInfo>> productCodeToInfoMap;

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
        private String warehouse;
//        private Double margin;
        private Boolean isActive;
        private Integer stockQuantity;

    }

    @Override
    public void generateAllPricings() throws RuntimeException
    {
        this.populatePurchasePriceMap();
        this.populateMapPrices();
        this.generateProductWarehouses();
        for(Map<String, ProductPriceInfo> map : this.productCodeToInfoMap.values())
            for(ProductPriceInfo p: map.values())
                if(p.isActive != null && p.isActive)
                    System.out.println("Active: " + p.sku + " with stock: " + p.stockQuantity + " in warehouse: " + p.warehouse);
        System.out.println("Hi");
    }

    private void populatePurchasePriceMap() throws RuntimeException
    {
        List<ProductPurchasePriceList> purchasePriceList = purchaseRepository.listAll();
        this.productCodeToInfoMap = new HashMap<>();
        productCodeToInfoMap = purchasePriceList.stream()
                .collect(
                        groupingBy(
                                ProductPurchasePriceList::getProductCode,
                                groupingBy(ProductPurchasePriceList::getWarehouseString, collectingAndThen(toList(), salesPricesForWarehouse->
                                {
                                    if(salesPricesForWarehouse.size() != 1)
                                    {
                                        log.error("Unexpected dupplication in db");
                                        throw new RuntimeException("Unexpected dupplication in db");
                                    }

                                    ProductPurchasePriceList p = salesPricesForWarehouse.get(0);
                                    return ProductPriceInfo.builder()
                                            .sku(           p.getProductCode())
                                            .purchasePrice( p.getPrice())
                                            .streetPriceVat(p.getProductSku().getStreetPriceVat())
                                            .vatValue(      p.getProductSku().getVatValue())
                                            .category(      p.getProductSku().getCategoryCodeString())
                                            .warehouse(     p.getSupplierWarehouseCode().getId())
                                            .isActive(false)
                                            .build();
                                })))

                );
        System.out.println("Hi");
    }

    private void populateMapPrices()
    {
        for(Map<String, ProductPriceInfo> map : this.productCodeToInfoMap.values())
            for(ProductPriceInfo p : map.values())
            {
                p.purchasePriceVat = p.streetPriceVat * CustomMarginCategoriesEnum.streetMarginByCategory(p.category);
                double vatlessGeneratedPrice = p.purchasePriceVat * (1D - p.vatValue);

                log.info("VatlessGeneratedPrice for product " + p.sku + " is: " + vatlessGeneratedPrice);

                if(vatlessGeneratedPrice > p.purchasePrice)
                {
                    p.generatedPrice = vatlessGeneratedPrice;

                    log.info("VatlessGeneratedPrice is greater than purchase price: " + p.purchasePrice);
                }
                else
                {
                    double vatlessStreetPrice = p.streetPriceVat * (1D - p.vatValue);
                    p.generatedPrice = Math.min(p.purchasePrice * Constants.DEFAULT_PRICE_INCREMENT, vatlessStreetPrice);

                    log.info("VatlessGeneratedPrice is lesser than purchase price: " + p.purchasePrice);
                    log.info("VatlessStreetPrice for product " + p.sku + " is: " + vatlessStreetPrice);
                    log.info("VatlessGeneratedPrice for product " + p.sku + " is: " + p.generatedPrice +
                            ". Min(" + p.purchasePrice * Constants.DEFAULT_PRICE_INCREMENT + ", " + vatlessStreetPrice +")");
                }
            }
    }

    private void generateProductWarehouses()
    {
        for(String productKey: this.productCodeToInfoMap.keySet())
        {
            List<ProductStock> stocks = stockRepository.findQuantityAndWarehouseByProductCode(productKey);
            stocks.forEach(s -> productCodeToInfoMap.get(productKey).get(s.getWarehouseString()).stockQuantity = s.getQuantity());

            String selectedWarehouse = selectBestWarehouse(stocks, productKey);
            if(selectedWarehouse != null)
                productCodeToInfoMap.get(productKey).get(selectedWarehouse).isActive = true;

        }
    }

    private String selectBestWarehouse(List<ProductStock> stocks, String productKey)
    {
        if (stocks.stream().filter(s -> s.getQuantity() > Constants.MINIMUM_WAREHOUSE_STOCK).anyMatch(s -> s.getWarehouseString().equals("M1")))
            return "M1";

        List<ProductStock> numerousFilteredStocksM2OrM4 = stocks.stream()
                .filter(s -> s.getQuantity() > Constants.MINIMUM_WAREHOUSE_STOCK)
                .filter(s -> ((s.getWarehouseString().equals("M2"))) || (s.getWarehouseString().equals("M4")))
                .collect(toList());
        if (!numerousFilteredStocksM2OrM4.isEmpty())
        {
            if (numerousFilteredStocksM2OrM4.size() == 1)
            {
                return numerousFilteredStocksM2OrM4.get(0).getWarehouseString();
            } else
            {
                ProductPriceInfo m2Info = productCodeToInfoMap.get(productKey).get("M2");
                double m2Margin = m2Info.generatedPrice - m2Info.purchasePrice;
                ProductPriceInfo m4Info = productCodeToInfoMap.get(productKey).get("M4");
                double m4Margin = m4Info.generatedPrice - m4Info.purchasePrice;

                if (m2Margin >= m4Margin) //Condizione di parita, priorita M2
                    return "M2";
                else
                    return "M4";
            }
        }

        Boolean hasM1 = stocks.stream()
                .anyMatch(s -> (s.getWarehouseString().equals("M1")));
        Boolean hasM2 = stocks.stream()
                .anyMatch(s -> (s.getWarehouseString().equals("M2")));
        if (hasM1 && hasM2)
        {
                ProductPriceInfo m1Info = productCodeToInfoMap.get(productKey).get("M1");
                ProductPriceInfo m2Info = productCodeToInfoMap.get(productKey).get("M2");

                if (m1Info.stockQuantity >= m2Info.stockQuantity) //Condizione di parita, priorita M1
                    return "M1";
                else
                    return "M2";
        }

        String mostMarginWh = initializeDefaultWh(productKey);
        double maxMargin = 0D;
        double margin;
        for(ProductPriceInfo p: productCodeToInfoMap.get(productKey).values())
        {
            margin = p.generatedPrice - p.purchasePrice;

            if(margin > maxMargin)
            {
                maxMargin = margin;
                mostMarginWh = p.warehouse;
            }
        }
        return mostMarginWh;
    }

    private String initializeDefaultWh(String productKey)
    {
        if(productCodeToInfoMap.get(productKey).containsKey("M1"))
            return "M1";
        if(productCodeToInfoMap.get(productKey).containsKey("M2"))
            return "M2";
        if(productCodeToInfoMap.get(productKey).containsKey("M3"))
            return "M3";

        //Diamo per scontato che abbia almeno un wh dato che e' stato generato partendo da un db consistente
        return "M4";
    }

    private void persistSalesPriceEntities()
    {
        List<ProductSalesPriceList> priceLists = new ArrayList<>();

        for(Map<String, ProductPriceInfo> map : this.productCodeToInfoMap.values())
            for(ProductPriceInfo p : map.values())
            {
                priceLists.add(
                        ProductSalesPriceList.builder()
                        .productSku(productRepository.findById(p.sku))
                        .streetPriceVat(p.streetPriceVat)
                        .finalPrice(p.generatedPrice)
                        .isActive(p.isActive)
                        .build()
                );

            }

        salesRepository.persist(priceLists);
    }

    @Override
    public Long countGeneratedPricings()
    {
        return null;
    }
}
