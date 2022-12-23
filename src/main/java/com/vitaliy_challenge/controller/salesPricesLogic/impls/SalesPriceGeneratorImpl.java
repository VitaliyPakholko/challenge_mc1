package com.vitaliy_challenge.controller.salesPricesLogic.impls;

import com.vitaliy_challenge.controller.rest.repositories.ProductPurchasePriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductSalesPriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductStockRepository;
import com.vitaliy_challenge.controller.salesPricesLogic.SalesPricesGenerator;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.Constants;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.CustomMarginCategoriesEnum;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductPurchasePrice;
import com.vitaliy_challenge.model.entities.ProductSalesPrice;
import com.vitaliy_challenge.model.entities.ProductStock;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.h2.expression.function.CurrentDateTimeValueFunction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.*;
import java.util.*;

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
//    @PostConstruct
    @Transactional
    public void deleteAllPricings()
    {
        salesRepository.deleteAll();
    }

    @Override
    public List<ProductSalesPrice> generateAllPricings() throws RuntimeException
    {
        this.deleteAllPricings();
        this.populatePurchasePriceMap();
        this.populateMapPrices();
        this.populateProductWarehouses();
        return this.createSalesPriceEntities();
    }

    private void populatePurchasePriceMap() throws RuntimeException
    {
        List<ProductPurchasePrice> purchasePriceList = purchaseRepository.listAll();
        this.productCodeToInfoMap = new HashMap<>();
        productCodeToInfoMap = purchasePriceList.stream()
                .collect(
                        groupingBy(
                                ProductPurchasePrice::getProductCode,
                                groupingBy(ProductPurchasePrice::getWarehouseString, collectingAndThen(toList(), salesPricesForWarehouse->
                                {
                                    if(salesPricesForWarehouse.size() != 1)
                                    {
                                        log.error("Unexpected dupplication in db");
                                        throw new RuntimeException("Unexpected dupplication in db");
                                    }

                                    ProductPurchasePrice p = salesPricesForWarehouse.get(0);
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

    private void populateProductWarehouses()
    {
        List<ProductStock> stocks = stockRepository.listAll();
        Map<String, List<ProductStock>> stockMap;

        stockMap = stocks.stream().collect(groupingBy(ProductStock::getProductSkuString));

        for(String productKey: this.productCodeToInfoMap.keySet())
        {
            if(stockMap.containsKey(productKey))
            {
                stockMap.get(productKey).forEach(s -> productCodeToInfoMap.get(productKey).get(s.getWarehouseString()).stockQuantity = s.getQuantity());

                String selectedWarehouse = selectBestWarehouse(stocks, productKey);
                if (selectedWarehouse != null)
                {
                    //Gli stock contengono tutti i warehouse prodotto ma non e' detto che nel listino vendita ci siano tutti i warehouse in stock
                    if(productCodeToInfoMap.get(productKey).containsKey(selectedWarehouse))
                        productCodeToInfoMap.get(productKey).get(selectedWarehouse).isActive = true;
                }
            }

        }

        log.info("Calculated best warehouses for all the products.");
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

        String mostMarginWh = initializeDefaultWH(productKey);
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

    private String initializeDefaultWH(String productKey)
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

//    @Transactional
    private List<ProductSalesPrice> createSalesPriceEntities()
    {
        List<ProductSalesPrice> priceList = new LinkedList<>();
        Date date = new Date();
        Instant inst = date.toInstant();
        LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
        Instant tomorrowMidnight = localDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        for(Map<String, ProductPriceInfo> map : this.productCodeToInfoMap.values())
            for(ProductPriceInfo p : map.values())
            {
                Product product = productRepository.findById(p.sku);
                priceList.add(
                        ProductSalesPrice.builder()
                                .productSku(product)
                                .streetPriceVat(p.streetPriceVat)
                                .finalPrice(p.generatedPrice)
                                .isActive(p.isActive)
                                .build()
                );

                //Unico appunto, qua considero che se e' attivo il listino di vendita, e' attiva anche l'offerta e viceversa
                if(CustomMarginCategoriesEnum.isCategoryPromotional(product.getCategoryCodeString()))
                    priceList.add(
                            ProductSalesPrice.builder()
                                    .productSku(product)
                                    .streetPriceVat(p.streetPriceVat)
                                    .finalPrice(p.generatedPrice * Constants.PROMOTIONAL_DISCOUNT)
                                    .isActive(p.isActive)
                                    .promotion(Constants.PROMOTIONAL_SLOGAN)
                                    .dateFrom(Instant.now())
                                    .dateTo(tomorrowMidnight)
                                    .build()
                    );
                //Avrei potuto filtrare e salvare solo i listini/promozioni attivi ma non c'e' nulla nella specifica a riguardo
            }
        return priceList;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void persistGeneratedPriceEntities(List<ProductSalesPrice> salesPriceListBatch)
    {
        log.info("About to persist the sales prices");
        salesRepository.persist(salesPriceListBatch);
        log.info("SUCCESS, persisted: " + salesPriceListBatch.size() + " sales prices.");
    }
}
