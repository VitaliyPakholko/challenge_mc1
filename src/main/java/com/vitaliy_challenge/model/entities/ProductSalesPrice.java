package com.vitaliy_challenge.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "PRODUCT_SALES_PRICE_LIST")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSalesPrice
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NonNull
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false/*, cascade = {CascadeType.PERSIST, CascadeType.MERGE}*/)
    @JoinColumn(name = "PRODUCT_SKU", nullable = false/*, insertable = false, updatable = false*/)
    @ToString.Exclude
    private Product productSku;

    @NonNull
    @Column(name = "STREET_PRICE_VAT", nullable = false)
    private Double streetPriceVat;

    @NonNull
    @Column(name = "FINAL_PRICE", nullable = false)
    private Double finalPrice;

    @Column(name = "PROMOTION")
    private String promotion;

    @Column(name = "DATE_FROM")
    private Instant dateFrom;

    @Column(name = "DATE_TO")
    private Instant dateTo;

    @NonNull
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductSalesPrice that = (ProductSalesPrice) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}