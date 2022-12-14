package com.vitaliy_challenge.controller.resources;

import com.vitaliy_challenge.controller.services.ProductService;
import com.vitaliy_challenge.model.dtos.concrete.ProductDto;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/product")
public class ProductResource
{
    @Inject
    ProductService productService;

    @GET
    @Path("/findbyId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductDto findProductById(@PathParam("id") String id)
    {
        return productService.findProductById(id);
    }

}
