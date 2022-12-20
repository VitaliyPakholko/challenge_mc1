package com.vitaliy_challenge.controller.rest.repositories;

import com.vitaliy_challenge.model.entities.ProductPurchasePriceList;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductPurchasePriceRepository implements PanacheRepositoryBase<ProductPurchasePriceList, Long>
{

}
