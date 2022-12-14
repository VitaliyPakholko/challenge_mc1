package com.vitaliy_challenge.controller.services.impls;

import com.vitaliy_challenge.controller.repositories.ProductRepository;
import com.vitaliy_challenge.controller.services.ProductService;
import com.vitaliy_challenge.model.dtos.concrete.ProductDto;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.mappers.impls.ProductMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Optional;

@ApplicationScoped
public class ProductServiceImpl implements ProductService
{
    @Inject
    ProductRepository productRepository;

    @Inject
    ProductMapper mapper;

    public ProductDto findProductById(String id)
    {
        Optional<Product> optionalProduct = productRepository.findByIdOptional(id);
        System.out.println(optionalProduct.orElse(null));
        return mapper.toDto(optionalProduct.orElse(null));
    }

}
