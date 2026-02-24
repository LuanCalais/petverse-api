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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;

@Path("/api/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Post", description = "Post endpoints")
public class PostResource {

    @Inject
    PostService postService;

    @POST
    @Operation(summary = "Create a new post")
    public Response create(@Valid PostCreateDTO dto) {
        PostResponseDTO response = postService.create(dto);
        return Response.status(Response.Status.CREATED).entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update pet")
    public PostResponseDTO update(@PathParam("id") Long id, @Valid PostUpdateDTO dto) {
        return postService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete post by id")
    public Response delete(@PathParam("id") Long id) {
        postService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get post by id")
    public PostResponseDTO findById(@PathParam("id") Long id) {
        return postService.findById(id);
    }

    @GET
    @Operation(summary = "Get posts")
    public List<PostResponseDTO> listAll() {
        List<PostResponseDTO> mock = new ArrayList<>();
        mock.add(new PostResponseDTO());
        return mock;
    }
}
