package com.petverse.domain.service;

import com.petverse.domain.dto.PostCreateDTO;
import com.petverse.domain.dto.PostResponseDTO;
import com.petverse.domain.dto.PostUpdateDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
import com.petverse.domain.entity.User;
import com.petverse.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostService {
    @Inject
    EntityManager em;

    @Transactional
    public PostResponseDTO create(PostCreateDTO dto) {
        User owner = User.findById(dto.ownerId);
        if (owner == null || !owner.active) {
            throw new ResourceNotFoundException("Owner not found with id: " + dto.ownerId);
        }

        List<Pet> pets = Pet.list("id in ?1", dto.petIds);
        if (pets.size() != dto.petIds.size()) {
            throw new ResourceNotFoundException("One or more pets not found");
        }

        Post post = new Post();
        post.content = dto.content;
        post.pets = pets;
        post.owner = owner;
        post.persist();

        return new PostResponseDTO(post);
    }

    @Transactional
    public PostResponseDTO update(Long id, PostUpdateDTO dto) {
        Post post = Post.findById(id);
        if (post == null || !post.active) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        if (dto.content != null) {
            post.content = dto.content;
        }
        if (dto.pets != null) {
            post.pets = dto.pets;
        }

        return new PostResponseDTO(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = Post.findById(id);
        if (post == null || !post.active) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        post.active = false;
    }

    public List<PostResponseDTO> list() {
        return Post.listActive()
                .stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PostResponseDTO findById(Long id) {
        Post post = Post.findById(id);
        if (post == null || !post.active) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        return new PostResponseDTO(post);
    }
}
