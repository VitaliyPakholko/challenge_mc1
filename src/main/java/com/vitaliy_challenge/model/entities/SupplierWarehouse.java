package com.vitaliy_challenge.model.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "SUPPLIER_WAREHOUSE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierWarehouse
{
    @Id
    @Column(name = "CODE", nullable = false, length = 50)
    @NonNull
    private String id;

    @OneToMany(mappedBy = "supplierWarehouseCode")
    @ToString.Exclude
    private Set<ProductStock> productStocks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "supplierWarehouseCode")
    @ToString.Exclude
    private Set<ProductPurchasePriceList> productPurchasePriceLists = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SupplierWarehouse that = (SupplierWarehouse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}