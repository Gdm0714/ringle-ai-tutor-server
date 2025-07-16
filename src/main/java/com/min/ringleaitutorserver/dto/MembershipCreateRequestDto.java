package com.min.ringleaitutorserver.dto;

import com.min.ringleaitutorserver.entity.Membership;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipCreateRequestDto {
    @NotNull
    private String name;

    @NotNull
    private Membership.MembershipType type;

    private Integer conversationLimit;


    @NotNull
    private Integer durationDays;

    private Integer price;

    public Membership toEntity() {
        return Membership.of(name, type, conversationLimit, durationDays, price);
    }

    public Membership toCustomEntity() {
        return Membership.createCustomMembership(name, type, conversationLimit, durationDays, price);
    }
}