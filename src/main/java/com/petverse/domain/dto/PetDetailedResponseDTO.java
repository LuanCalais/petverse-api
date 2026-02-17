package com.petverse.domain.dto;

import com.petverse.domain.entity.Pet;

import java.util.List;

public class PetDetailedResponseDTO extends PetResponseDTO {

    public Integer friendsCount;

    public List<Long> friendPetIds;

    public Integer postsCount;

    public List<Long> recentPostIds;

    public Long totalLikes;

    public PetDetailedResponseDTO(
            Pet pet,
            Integer friendsCount,
            List<Long> friendPetIds,
            Integer postsCount,
            List<Long> recentPostIds,
            Long totalLikes,
            Integer followersCount) {

        super(pet);

        this.friendsCount = friendsCount;
        this.friendPetIds = friendPetIds;
        this.postsCount = postsCount;
        this.recentPostIds = recentPostIds;
        this.totalLikes = totalLikes;
        this.followersCount = followersCount;
    }

    public Integer followersCount;
}