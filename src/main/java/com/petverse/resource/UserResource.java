package com.petverse.resource;

import com.petverse.domain.dto.UserCreateDTO;
import com.petverse.domain.dto.UserResponseDTO;
import com.petverse.domain.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @POST
    public Response create(@Valid UserCreateDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public List<UserResponseDTO> listAll() {
        return userService.listAll();
    }

    @GET
    @Path("/email/{email}")
    public UserResponseDTO findByEmail(@PathParam("email") String email) {
        return userService.findByEmail(email);
    }
}
