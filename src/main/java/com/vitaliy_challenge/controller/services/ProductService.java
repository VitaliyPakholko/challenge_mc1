package com.vitaliy_challenge.controller.services;

import com.vitaliy_challenge.model.entities.Product;

import javax.ws.rs.core.Response;
import java.util.Optional;

public interface ProductService
{
    public Response findProductById(String id);
}
