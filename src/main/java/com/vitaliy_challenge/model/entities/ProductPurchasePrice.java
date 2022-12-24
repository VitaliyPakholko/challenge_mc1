package com.vitaliy_challenge.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PRODUCT_PURCHASE_PRICE_LIST")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPurchasePrice
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NonNull
    @Column(name = "PRICE", nullable = false)
    private Double price;

    @NonNull
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_SKU", nullable = false)
    @ToString.Exclude
    private Product productSku;

    @NonNull
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUPPLIER_WAREHOUSE_CODE", nullable = false)
    @ToString.Exclude
    private SupplierWarehouse supplierWarehouseCode;

    @JsonIgnore
    @Column(name = "PRODUCT_SKU", insertable = false, updatable = false)
    private String productCode;

    // Attributi estratti come stringa per ottimizzare query

    @JsonIgnore
    @Column(name = "SUPPLIER_WAREHOUSE_CODE", insertable = false, updatable = false)
    private String warehouseString;

    // -------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductPurchasePrice that = (ProductPurchasePrice) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}