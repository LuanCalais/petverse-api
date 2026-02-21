package com.petverse.domain.dto;

import com.petverse.domain.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    public Long id;
    public String content;
    public Long ownerId;
    public List<PetResponseDTO> pets;
    public LocalDateTime createdAt;
    public Boolean active;
    public LocalDateTime updatedAt;


    public PostResponseDTO() {
    }

    public PostResponseDTO(Post post) {
        this.id = post.id;
        this.content = post.content;
        this.ownerId = post.owner != null ? post.owner.id : null;
        this.createdAt = post.createdAt;
        this.updatedAt = post.updatedAt;
        this.active = post.active;
        this.pets = post.pets != null
                ? post.pets.stream().map(PetResponseDTO::new).toList()
                : List.of();
    }
}
