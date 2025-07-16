/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.models.Producto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
public class ProductoService {

    @PersistenceContext(unitName = "AplicacionMundialPU")
    EntityManager entityManager;

    @GET
    public Response getAllProductos() {
        try {
            Query query = entityManager.createQuery("SELECT p FROM Producto p");
            List<Producto> productos = query.getResultList();
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(productos)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .entity("{\"error\": \"Error al obtener los productos.\"}")
                    .build();
        }
    }
}
