package com.vitaliy_challenge.controller.repositories;

import com.vitaliy_challenge.controller.restApis.Requests.PagedProductRequest;
import com.vitaliy_challenge.controller.restApis.Responses.PagedProductResponse;
import com.vitaliy_challenge.model.dtos.concrete.ProductDtoSlim;
import com.vitaliy_challenge.model.entities.Category;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.entities.ProductStock;
import com.vitaliy_challenge.model.entities.SupplierWarehouse;
import com.vitaliy_challenge.model.mappers.impls.ProductSlimConverter;
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
    ProductSlimConverter converter;

    private final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
    private final CriteriaQuery<Product> cq = cb.createQuery(Product.class).distinct(true);
    private final Root<Product> productRoot = cq.from(Product.class);
    private TypedQuery<Product> typedQuery;



    public PagedProductResponse getPagedProducts(PagedProductRequest request)
    {
        this.createQueryWithPredicates(this.assemblePredicates(request));
        List<Product> products = typedQuery.getResultList();

        List<ProductDtoSlim> productDtoFulls = this.mapToSlimDto(products);

        return PagedProductResponse.builder()
                .results(productDtoFulls)
                .build();
    }

    private Predicate[] assemblePredicates(PagedProductRequest request)
    {
        List<Predicate> predicates = new ArrayList<>();

        if(request.getWithStock() != null && request.getWithStock().equals(Boolean.TRUE))
            predicates.add(cb.and(
                    cb.in(productRoot).value(findProductsWithStock(cq))
                    ));

        if(request.getCategoryCodes() != null && !request.getWarehouseCode().isEmpty())
            predicates.add(cb.and(
                    cb.in(productRoot).value(findProductsByWarehouseCode(cq, request.getWarehouseCode()))
            ));

        if(request.getWarehouseCode() != null && !request.getWarehouseCode().isEmpty())
            predicates.add(cb.and(
                    cb.in(productRoot).value(findProductsByCategory(cq, request.getCategoryCodes()))
            ));

        return predicates.toArray(new Predicate[0]);
    }

    private void createQueryWithPredicates(Predicate...predicates)
    {
        cq.where(predicates);
        this.typedQuery = this.getEntityManager().createQuery(cq);
    }

    private Subquery<Product> findProductsWithStock(CriteriaQuery<Product> cq)
    {
        Subquery<Product> subquery = cq.subquery(Product.class);
        Root<ProductStock> from = subquery.from(ProductStock.class);
        Join<ProductStock,Product> join = from.join("productSku");

        subquery.select(join).distinct(true);
        return subquery;
    }

    private Subquery<Product> findProductsByWarehouseCode(CriteriaQuery<Product> cq, String warehouseCode)
    {
        Subquery<Product> subquery = cq.subquery(Product.class);
        Root<SupplierWarehouse> supplier = subquery.from(SupplierWarehouse.class);
        Join<SupplierWarehouse,ProductStock> join = supplier.join("productStocks");
        Join<ProductStock,Product> join2 = join.join("productSku");

        subquery.distinct(true);
        subquery.select(join2);
        subquery.where(this.cb.equal(join.get("supplierWarehouseCode").get("id"), warehouseCode));

        return subquery;
    }

    private Subquery<Product> findProductsByCategory(CriteriaQuery<Product> cq, List<String> categories)
    {
        Subquery<Product> subquery = cq.subquery(Product.class);
        Root<Product> from = subquery.from(Product.class);
        Join<Category,Product> join = from.join("categoryCode");
        List<Predicate> orPredicates = new ArrayList<>();

        subquery.distinct(true);
        subquery.select(from);
        
        for(String cat: categories)
            orPredicates.add(this.cb.equal(join.get("id"), cat));

        subquery.where(cb.or(orPredicates.toArray(new Predicate[0])));
        return subquery;
    }

    private List<ProductDtoSlim> mapToSlimDto(List<Product> products)
    {
        return products.stream()
        .map(converter::toDto)
        .collect(Collectors.toList());
    }

}
