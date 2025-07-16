/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    @PersistenceContext(unitName = "AplicacionMundialPU")
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            if (entityManager == null) {
                entityManager = PersistenceManager.getInstance()
                        .getEntityManagerFactory()
                        .createEntityManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obtener todos los competidores
    @GET
    public Response getAll() {
        try {
            Query q = entityManager.createQuery("SELECT u FROM Competitor u ORDER BY u.surname ASC");
            List<Competitor> competitors = q.getResultList();
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(competitors)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .entity("{\"error\": \"Error al obtener los competidores.\"}")
                    .build();
        }
    }

    // Crear nuevo competidor
    @POST
    public Response createCompetitor(CompetitorDTO competitor) {
        Competitor c = new Competitor();
        JSONObject rta = new JSONObject();

        c.setAddress(competitor.getAddress());
        c.setAge(competitor.getAge());
        c.setCellphone(competitor.getCellphone());
        c.setCity(competitor.getCity());
        c.setCountry(competitor.getCountry());
        c.setName(competitor.getName());
        c.setSurname(competitor.getSurname());
        c.setTelephone(competitor.getTelephone());
        c.setVehicle(competitor.getVehicle());
        c.setProducto(competitor.getProducto()); // si CompetitorDTO tiene el producto

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(c);
            entityManager.getTransaction().commit();
            entityManager.refresh(c);
            rta.put("competitor_id", c.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\": \"Error al guardar el competidor.\"}")
                    .build();
        }

        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(rta.toJSONString())
                .build();
    }

    // Crear competidor con vehículo
    @POST
    @Path("/vehicle")
    public Response createCompetitorWithVehicle(CompetitorDTO competitor) {
        return createCompetitor(competitor); // usa misma lógica del POST general
    }

    // Obtener competidores por nombre exacto
   @GET
@Path("{name}")
@Produces(MediaType.APPLICATION_JSON)
public Response getCompetitorsByName(@PathParam("name") String name) {
 TypedQuery<Competitor> query = (TypedQuery<Competitor>) 
entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name = :name");
 List<Competitor> competitors =query.setParameter("name", name).getResultList();
 return Response.status(200).header("Access-Control-Allow-Origin", 
"*").entity(competitors).build();
}


    // Obtener nombres de competidores que comienzan por la letra A
    @GET
    @Path("/startswith/a")
    public Response getCompetitorsStartingWithA() {
        try {
            Query query = entityManager.createQuery(
                    "SELECT c.name FROM Competitor c WHERE UPPER(c.name) LIKE 'A%'");
            List<String> nombres = query.getResultList();

            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(nombres)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .entity("{\"error\": \"Error al obtener nombres.\"}")
                    .build();
        }
    }

    // Soporte para CORS
    @OPTIONS
    public Response cors(@Context HttpHeaders requestHeaders) {
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "AUTHORIZATION, content-type, accept")
                .build();
    }
}
