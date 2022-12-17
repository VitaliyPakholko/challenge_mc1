package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.ProductDtoSlim;
import com.vitaliy_challenge.model.entities.Product;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductSlimConverter
{
    public ProductDtoSlim toDto(Product p)
    {
        return ProductDtoSlim.builder()
                .id(p.getId())
                .mpn(p.getMpn())
                .streetPriceVat(p.getStreetPriceVat())
                .vatValue(p.getVatValue())
                .categoryCodeString(p.getCategoryCodeString())
                .build();
    }
}
