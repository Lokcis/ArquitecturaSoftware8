/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import java.util.List;

@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

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

    @GET
    public Response getAll() {
        try {
            Query q = entityManager.createQuery("SELECT u FROM Competitor u ORDER BY u.surname ASC");
            List<Competitor> competitors = q.getResultList();

            System.out.println("Entrando al método getAll()");
            System.out.println("EntityManager abierto: " + entityManager.isOpen());

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
        c.setProducto(competitor.getProducto());

        // NUEVO: Setear email y password si los tiene
        c.setEmail(competitor.getEmail());
        c.setPassword(competitor.getPassword());

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

    @POST
    @Path("/vehicle")
    public Response createCompetitorWithVehicle(CompetitorDTO competitor) {
        return createCompetitor(competitor); // Reutiliza el método anterior
    }

    @GET
    @Path("{name}")
    public Response getCompetitorsByName(@PathParam("name") String name) {
        try {
            Query q = entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name = :name");
            q.setParameter("name", name);
            List<Competitor> competitors = q.getResultList();
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(competitors)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .entity("{\"error\": \"Error al buscar competidores por nombre.\"}")
                    .build();
        }
    }

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

    @OPTIONS
    public Response cors(@Context HttpHeaders requestHeaders) {
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "AUTHORIZATION, content-type, accept")
                .build();

    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(CompetitorDTO credentials) {
        try {
            Query q = entityManager.createQuery(
                    "SELECT c FROM Competitor c WHERE c.email = :email AND c.password = :password");
            q.setParameter("email", credentials.getEmail());
            q.setParameter("password", credentials.getPassword());

            List<Competitor> result = q.getResultList();

            if (result.isEmpty()) {
                return Response.status(401)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\": \"Credenciales inválidas\"}")
                        .build();
            }

            Competitor c = result.get(0);
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(c)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\": \"Error interno al realizar login.\"}")
                    .build();
        }
    }

}
