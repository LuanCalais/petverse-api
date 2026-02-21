package com.petverse.resource;

import com.petverse.domain.dto.PostCreateDTO;
import com.petverse.domain.dto.PostResponseDTO;
import com.petverse.domain.dto.PostUpdateDTO;
import com.petverse.domain.service.PostService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/api/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    PostService postService;

    @POST
    public Response create(@Valid PostCreateDTO dto) {
        PostResponseDTO responde = postService.create(dto);
        return Response.status(Response.Status.CREATED).entity(responde).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid PostUpdateDTO dto) {
        PostResponseDTO responde = new PostResponseDTO();
        return Response.status(Response.Status.OK).entity(responde).type(MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public PostResponseDTO findById(@PathParam("id") Long id) {
        return new PostResponseDTO();
    }

    @GET
    public List<PostResponseDTO> listAll() {
        List<PostResponseDTO> mock = new ArrayList<>();
        mock.add(new PostResponseDTO());
        return mock;
    }
}
