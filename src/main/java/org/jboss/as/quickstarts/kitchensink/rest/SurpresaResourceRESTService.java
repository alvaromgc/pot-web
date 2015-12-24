/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Game;
import org.jboss.as.quickstarts.kitchensink.model.Guess;
import org.jboss.as.quickstarts.kitchensink.model.MediaDesv;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.model.Ocorrencia;
import org.jboss.as.quickstarts.kitchensink.service.GameRegistration;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.jboss.as.quickstarts.kitchensink.service.SurpresaEngine;
import org.jboss.as.quickstarts.kitchensink.service.SurpresaService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/surpresa")
@RequestScoped
public class SurpresaResourceRESTService {
    
    @Inject
    private Logger log;

    @Inject
    private SurpresaService surpresaService;
    
    @Inject
    private GameRegistration gameRegistration;
     
    @Inject
    private List<Game> allGames;

    @GET
    @Path("/resultados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> listAllGames() {
        return allGames;
    }

    
    @GET
    @Path("/medDev")
    @Produces(MediaType.APPLICATION_JSON)
    public MediaDesv returnMediaDevp(@QueryParam("ini") Integer inicio, @QueryParam("fim") Integer fim) {
        MediaDesv meddev = new MediaDesv();
        Response.ResponseBuilder builder = null;
        try {
			meddev = surpresaService.listaMediaDevP(inicio, fim);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
        builder = Response.ok(meddev);
        return meddev;
    }
    
    @POST
    @Path("/surpresinhas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Game> returnMediaDevp(Guess guess) {
    	List<Game> games = new ArrayList<Game>();
        Response.ResponseBuilder builder = null;
        try {
        	games = surpresaService.gerarSurpresas(guess, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
        builder = Response.ok(games);
        return games;
    }
    
    @GET
    @Path("/historicoOcorrencia")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ocorrencia> returnListaOcorrencias(@QueryParam("ini") Integer inicio, @QueryParam("fim") Integer fim) {
        List<Ocorrencia> ocorrencias = new ArrayList<>();
        try {
			ocorrencias = surpresaService.listaOcorrencia(inicio, fim);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
		}
        return ocorrencias;
    }
    
    @GET
    @Path("/numerosOcorrencia")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ocorrencia> returnListaNumerosOcorrencia() {
        List<Ocorrencia> ocorrencias = new ArrayList<>();
        try {
			ocorrencias = surpresaService.listaNumerosOcorrencia();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
		}
        return ocorrencias;
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGame(Game game) {

        Response.ResponseBuilder builder = null;

        try {

            gameRegistration.register(game);

            // Create an "ok" response
            builder = Response.ok();
        }catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        }catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    } 

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
    
    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(Member member) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates member using bean validation
            validateMember(member);

            registration.register(member);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
    */
   
}
