package com.vitaliy_challenge.controller.repositories;

import com.vitaliy_challenge.controller.restApis.Requests.PagedProductRequest;
import com.vitaliy_challenge.controller.restApis.Responses.PagedProductResponse;
import com.vitaliy_challenge.model.dtos.concrete.ProductDto;
import com.vitaliy_challenge.model.entities.Category;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductStock;
import com.vitaliy_challenge.model.entities.SupplierWarehouse;
import com.vitaliy_challenge.model.mappers.impls.ProductMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<Product, String>
{
    @Inject
    ProductMapper mapper;

    private final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();

    public PagedProductResponse getPagedProducts(PagedProductRequest request)
    {

        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> productRoot = cq.from(Product.class);
        Join<Product, ProductStock> join = productRoot.join("productStocks");
        cq.distinct(true);
        cq.where(cb.and(
                cb.in(join).value(findProductsWithStock(cq)),
                cb.in(join).value(findProductsByWarehouseCode(cq, request.getWarehouseCode()))//,
//                cb.in(join).value(findProductsByCategory(cq, request.getCategoryCodes()))
        ));

        TypedQuery<Product> typedQuery = this.getEntityManager().createQuery(cq);

        List<Product> products = typedQuery.getResultList();

        List<ProductDto> productDtos =
                products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return PagedProductResponse.builder()
                .results(productDtos)
                .build();
    }

//    public Predicate buildPredicates(PagedProductRequest request, CriteriaBuilder cb)
//    {
//        List<Predicate> andPredicates = new ArrayList<>();
//        List<Predicate> orPredicates = new ArrayList<>();
//
//        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
//        Root<Product> productRoot = cq.from(Product.class);
//
//
//        if(request.getWithStock() != null && request.getWithStock().equals(Boolean.TRUE))
//        {
//            Join<Product, ProductStock> join = productRoot.join("productStocks", JoinType.LEFT);
//
////            Predicate p = productRoot.get("productStocks").isNotNull();
//            Predicate p = cb.in(join);
//            andPredicates.add(p);
////            cb.and(p);
//        }
//
//        if(request.getCategoryCodes() != null && !request.getWarehouseCode().isEmpty())
//        {
//            for(String s : request.getCategoryCodes())
//            {
//                Predicate p = cb.equal(productRoot.get("categoryCode").get("id"), s);
//                orPredicates.add(p);
//                cb.or(p);
//            }
//        }
//
//        if(request.getWarehouseCode() != null && !request.getWarehouseCode().isEmpty())
//        {
//            Predicate p = cb.equal(productRoot.get("productStocks").get("supplierWarehouseCode").get("id"), request.getWarehouseCode());
//            andPredicates.add(p);
//            cb.and(p);
//        }
//        return null;
//    }

    public Subquery<ProductStock> findProductsWithStock(CriteriaQuery<Product> cq)
    {
        Subquery<ProductStock> subquery = cq.subquery(ProductStock.class);
        Root<Product> from = subquery.from(Product.class);
        Join<Product, ProductStock> join = from.join("productStocks");

        subquery.select(join).distinct(true);
        return subquery;
    }

    public Subquery<ProductStock> findProductsByWarehouseCode(CriteriaQuery<Product> cq, String warehouseCode)
    {
        Subquery<ProductStock> subquery = cq.subquery(ProductStock.class);
        Root<SupplierWarehouse> from = subquery.from(SupplierWarehouse.class);
        Join<SupplierWarehouse,ProductStock> join = from.join("productStocks");

        subquery.distinct(true);
        subquery.select(join);
        subquery.where(this.cb.equal(join.get("supplierWarehouseCode").get("id"), warehouseCode));

        return subquery;
    }

//    public Subquery<Product> findProductsByCategory(CriteriaQuery<Product> cq, List<String> categories)
//    {
//        Subquery<Product> subquery = cq.subquery(Product.class);
//        Root<Product> from = subquery.from(Product.class);
//        Join<Category,Product> join = from.join("products");
//
//        subquery.distinct(true);
//        subquery.select(join);
//        for(String cat: categories)
//            subquery.where(this.cb.equal(join.get("id"), cat));
//
//        return subquery;
//    }

}
