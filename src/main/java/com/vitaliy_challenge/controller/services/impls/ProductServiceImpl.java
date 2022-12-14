package com.vitaliy_challenge.controller.services.impls;

import com.vitaliy_challenge.controller.repositories.ProductRepository;
import com.vitaliy_challenge.controller.services.ProductService;
import com.vitaliy_challenge.model.entities.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Optional;

@ApplicationScoped
public class ProductServiceImpl implements ProductService
{
    @Inject
    ProductRepository productRepository;

    public Response findProductById(String id)
    {
        Optional<Product> optionalProduct = productRepository.findByIdOptional(id);
        if(optionalProduct.isPresent())
            return Response.ok(optionalProduct.get()).build();
        else
            return Response.noContent().build();
    }

}
