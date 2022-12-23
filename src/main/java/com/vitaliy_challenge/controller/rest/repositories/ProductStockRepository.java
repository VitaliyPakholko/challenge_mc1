package com.vitaliy_challenge.controller.rest.repositories;

import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductSalesPriceList;
import com.vitaliy_challenge.model.entities.ProductStock;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class ProductStockRepository implements PanacheRepositoryBase<ProductStock, Long>
{
    private final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
    private final CriteriaQuery<ProductStock> cq = cb.createQuery(ProductStock.class);
    private final Root<ProductStock> stockRoot = cq.from(ProductStock.class);


    public List<ProductStock> findQuantityAndWarehouseByProductCode(String productCode)
    {
        cq.select(stockRoot);
        cq.where(cb.equal(stockRoot.get("productSku").get("id"), productCode));
        cq.orderBy(cb.asc(stockRoot.get("supplierWarehouseCode")));
        TypedQuery<ProductStock> typedQuery = this.getEntityManager().createQuery(cq);
        return typedQuery.getResultList();
    }
}
