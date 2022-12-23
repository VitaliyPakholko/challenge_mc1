package com.vitaliy_challenge.controller.rest.repositories;

import com.vitaliy_challenge.model.entities.ProductSalesPrice;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductSalesPriceRepository implements PanacheRepositoryBase<ProductSalesPrice, Long>
{
}
