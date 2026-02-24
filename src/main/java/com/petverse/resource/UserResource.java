package com.petverse.resource;

import com.petverse.domain.dto.UserCreateDTO;
import com.petverse.domain.dto.UserResponseDTO;
import com.petverse.domain.dto.UserUpdateDTO;
import com.petverse.domain.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User", description = "User endpoints")
public class UserResource {
    @Inject
    UserService userService;

    @POST
    @Operation(summary = "Create a new user")
    public Response create(@Valid UserCreateDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update user")
    public UserResponseDTO update(@PathParam("id") Long id, @Valid UserUpdateDTO dto) {
        return userService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete user by id")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Operation(summary = "Get users")
    public List<UserResponseDTO> listAll() {
        return userService.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by id")
    public UserResponseDTO findById(@PathParam("id") Long id) {
        return userService.findById(id);
    }

    @GET
    @Path("/email/{email}")
    @Operation(summary = "Get user by e-mail")
    public UserResponseDTO findByEmail(@PathParam("email") String email) {
        return userService.findByEmail(email);
    }
}
