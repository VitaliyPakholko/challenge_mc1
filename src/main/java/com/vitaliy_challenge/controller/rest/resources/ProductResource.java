package com.vitaliy_challenge.controller.rest.resources;

import com.vitaliy_challenge.controller.rest.communication.requests.PagedProductRequest;
import com.vitaliy_challenge.controller.rest.services.ProductService;
import com.vitaliy_challenge.controller.salesPricesLogic.SalesPricesGenerator;
import com.vitaliy_challenge.model.entities.ProductSalesPrice;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/product")
public class ProductResource
{
    @Inject
    ProductService productService;

    @Inject
    SalesPricesGenerator generator;

    @GET
    @Path("/productById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findProductById(@PathParam("id") String id)
    {
        return Response.ok(productService.productById(id)).build();
    }

    @POST
    @Path("/filteredProducts")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response filterProducts(PagedProductRequest request)
    {
        return Response.ok(productService.filteredProducts(request)).build();
    }

    @GET
    @Path("/generateSalesPricings")
    public void generatePricings()
    {
        persistPricings( generator.generateAllPricings());
    }

    @GET
    @Path("/persistPricings")
    @Transactional      //PLACEHOLDER per raggirare momentaneamente i problemi con la transazione rest
    public void persistPricings(List<ProductSalesPrice> productSalesPrices)
    {
        generator.persistGeneratedPriceEntities(productSalesPrices);
    }

}
