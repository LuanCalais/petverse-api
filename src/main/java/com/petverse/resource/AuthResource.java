package com.petverse.resource;

import com.petverse.domain.dto.AuthResponseDTO;
import com.petverse.domain.dto.RegisterRequestDTO;
import com.petverse.domain.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
}
