package com.vitaliy_challenge.controller.rest.repositories;

import com.vitaliy_challenge.model.entities.ProductStock;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductStockRepository implements PanacheRepositoryBase<ProductStock, Long>
{



//    public List<ProductStock> findQuantityAndWarehouseByProductCode(String productCode)
//    {
//        CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<ProductStock> cq = cb.createQuery(ProductStock.class);
//        Root<ProductStock> stockRoot = cq.from(ProductStock.class);
//        cq.select(stockRoot);
//        cq.where(cb.equal(stockRoot.get("productSkuString"), productCode));
////        cq.orderBy(cb.asc(stockRoot.get("supplierWarehouseCode")));
//        TypedQuery<ProductStock> typedQuery = this.getEntityManager().createQuery(cq);
//        return typedQuery.getResultList();
//    }
}
