package com.min.ringleaitutorserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMembershipAssignRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long membershipId;
}