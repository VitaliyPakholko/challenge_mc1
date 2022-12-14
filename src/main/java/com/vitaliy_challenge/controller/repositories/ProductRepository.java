package com.vitaliy_challenge.controller.repositories;

import com.vitaliy_challenge.model.entities.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<Product, String>
{

}
