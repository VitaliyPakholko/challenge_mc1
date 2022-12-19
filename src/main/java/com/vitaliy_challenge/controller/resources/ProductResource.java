package com.vitaliy_challenge.controller.resources;

import com.vitaliy_challenge.controller.communication.requests.PagedProductRequest;
import com.vitaliy_challenge.controller.services.ProductService;

import javax.inject.Inject;
import javax.ws.rs.*;
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
    public Response findProductById(@PathParam("id") String id)
    {
        return Response.ok(productService.productById(id)).build();
    }

    @POST
    @Path("/filteredProduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response filterProducts(PagedProductRequest request)
    {
        return Response.ok(productService.filteredProducts(request)).build();
    }

}
