package com.vitaliy_challenge.controller.resources;

import com.vitaliy_challenge.controller.services.ProductService;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/product")
public class ProductResource
{
    @Inject
    ProductService productService;

    @GET
    @Path("/findbyId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findProductById(@RestPath String id)
    {
        return productService.findProductById(id);
    }

}
