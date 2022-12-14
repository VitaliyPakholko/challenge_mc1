package com.vitaliy_challenge.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category
{
    @Id
    @Column(name = "CODE", nullable = false, length = 5)
    @NonNull
    private String id;

    @NonNull
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryCode")
    @ToString.Exclude
    private Set<Product> products = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}