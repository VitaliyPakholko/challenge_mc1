package com.vitaliy_challenge.model.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PRODUCT")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKU", nullable = false, length = 50)
    private String id;

    @NonNull
    @Column(name = "MPN", nullable = false)
    private String mpn;

    @NonNull
    @Column(name = "STREET_PRICE_VAT", nullable = false)
    private Double streetPriceVat;

    @NonNull
    @Column(name = "VAT_VALUE", nullable = false)
    private Double vatValue;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_CODE", nullable = false)
    @ToString.Exclude
    private Category categoryCode;

    @OneToMany(mappedBy = "productSku")
    @ToString.Exclude
    private Set<ProductStock> productStocks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productSku")
    @ToString.Exclude
    private Set<ProductPurchasePriceList> productPurchasePriceLists = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productSku")
    @ToString.Exclude
    private Set<ProductSalesPriceList> productSalesPriceLists = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}