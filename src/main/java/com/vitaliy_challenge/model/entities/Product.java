package com.vitaliy_challenge.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product
{
    @Id
    @Column(name = "SKU", nullable = false, length = 50)
    @NonNull
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

    @JsonBackReference
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_CODE", nullable = false)
    @ToString.Exclude
    private Category categoryCode;

    @JsonManagedReference
    @OneToMany(mappedBy = "productSku")
    @ToString.Exclude
    private Set<ProductStock> productStocks = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "productSku")
    @ToString.Exclude
    private Set<ProductPurchasePrice> productPurchasePriceLists = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "productSku", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ProductSalesPrice> productSalesPrices = new LinkedHashSet<>();

    // Attributi estratti come stringa per ottimizzare query

    @JsonIgnore
    @Column(name = "CATEGORY_CODE", insertable = false, updatable = false)
    private String categoryCodeString;

    // -------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}