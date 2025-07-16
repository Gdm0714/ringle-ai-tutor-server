package com.min.ringleaitutorserver.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MembershipType type;

    private Integer conversationLimit;

    private Integer analysisLimit;

    @NotNull
    private Integer durationDays;

    private Integer price;

    @NotNull
    @Builder.Default
    private Boolean isCustom = false;

    public static Membership of(String name, MembershipType type, Integer conversationLimit, 
                               Integer analysisLimit, Integer durationDays, Integer price) {
        return Membership.builder()
                .name(name)
                .type(type)
                .conversationLimit(conversationLimit)
                .analysisLimit(analysisLimit)
                .durationDays(durationDays)
                .price(price)
                .build();
    }

    public static Membership createCustomMembership(String name, MembershipType type, 
                                                   Integer conversationLimit, Integer analysisLimit, 
                                                   Integer durationDays, Integer price) {
        return Membership.builder()
                .name(name)
                .type(type)
                .conversationLimit(conversationLimit)
                .analysisLimit(analysisLimit)
                .durationDays(durationDays)
                .price(price)
                .isCustom(true)
                .build();
    }

    public enum MembershipType {
        B2C, B2B
    }
}
