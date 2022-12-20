package com.vitaliy_challenge.controller.salesPricesLogic.impls;

import com.vitaliy_challenge.controller.rest.repositories.ProductPurchasePriceRepository;
import com.vitaliy_challenge.controller.rest.repositories.ProductSalesPriceRepository;
import com.vitaliy_challenge.controller.salesPricesLogic.SalesPricesGenerator;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.Constants;
import com.vitaliy_challenge.controller.salesPricesLogic.constants.CustomMarginCategoriesEnum;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductPurchasePriceList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SalesPriceGeneratorImpl implements SalesPricesGenerator
{
    @Inject
    ProductPurchasePriceRepository purchaseRepository;

    @Inject
    ProductSalesPriceRepository salesRepository;

    @Override
    @PostConstruct
    @Transactional
    public void deleteAllPricings()
    {
        return;
//        salesRepository.deleteAll();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPriceInfo
    {
        private Double purchasePrice;
        private Double purchasePriceVat;
        private Double streetPriceVat;
        private Double vatValue;
        private String category;
        private Double generatedPrice;
    }

    @Override
    public void generateAllPricings()
    {

        List<ProductPurchasePriceList> purchasePriceList = purchaseRepository.listAll();
        Map<String, ProductPriceInfo> productPriceMap = new HashMap<>();
        purchasePriceList.forEach(
                p -> productPriceMap.put(
                        p.getProductCode(),
                        ProductPriceInfo.builder()
                                .purchasePrice( p.getPrice())
                                .streetPriceVat(p.getProductSku().getStreetPriceVat())
                                .vatValue(      p.getProductSku().getVatValue())
                                .category(      p.getProductSku().getCategoryCodeString())
                                .build()

                ));

        for(ProductPriceInfo p : productPriceMap.values())
        {
            double vatGeneratedPrice = p.streetPriceVat * CustomMarginCategoriesEnum.streetMarginByCategory(p.category);
            double vatlessGeneratedPrice = vatGeneratedPrice * (1D - p.vatValue);

            if(vatlessGeneratedPrice > p.purchasePrice)
                p.generatedPrice = vatlessGeneratedPrice;
            else
            {
                double vatlessStreetPrice = p.streetPriceVat * (1D - p.vatValue);
                p.generatedPrice = Math.min(p.purchasePrice * Constants.DEFAULT_PRICE_INCREMENT, vatlessStreetPrice);
            }
        }
    }

    @Override
    public Long countGeneratedPricings()
    {
        return null;
    }
}
