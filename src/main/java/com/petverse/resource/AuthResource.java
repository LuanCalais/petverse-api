package com.petverse.resource;

import com.petverse.domain.dto.AuthResponseDTO;
import com.petverse.domain.dto.LoginRequestDTO;
import com.petverse.domain.dto.RegisterRequestDTO;
import com.petverse.domain.dto.UserAuthResponseDTO;
import com.petverse.domain.service.AuthService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Operation(summary = "Register a new user")
    public Response register(@Valid RegisterRequestDTO dto) {
        AuthResponseDTO response = authService.register(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    @Operation(summary = "Register a new user")
    public Response login(@Valid LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/me")
    @Authenticated
    @Operation(summary = "Get the current authenticated user")
    public Response me(@Context SecurityContext securityContext) {
        String email = securityContext.getUserPrincipal().getName();
        UserAuthResponseDTO user = authService.me(email);
        return Response.status(Response.Status.OK).entity(user).build();
    }
}
