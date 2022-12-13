package com.vitaliy_challenge.model.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PRODUCT_STOCK")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_SKU", nullable = false)
    @ToString.Exclude
    private Product productSku;

    @NonNull
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUPPLIER_WAREHOUSE_CODE", nullable = false)
    @ToString.Exclude
    private SupplierWarehouse supplierWarehouseCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductStock that = (ProductStock) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}