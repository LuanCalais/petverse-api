package com.petverse.resource;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
import com.petverse.domain.service.PetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetResource {

    @Inject
    PetService petService;

    @POST
    public Response create(@Valid PetCreateDTO dto) {
        PetResponseDTO response = petService.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    public PetResponseDTO findById(@PathParam("id") Long id) {
        return petService.findById(id);
    }

    @GET
    public List<PetResponseDTO> listAll() {
        return petService.listAll();
    }

}
