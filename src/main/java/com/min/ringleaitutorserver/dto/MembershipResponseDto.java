package com.min.ringleaitutorserver.dto;

import com.min.ringleaitutorserver.entity.Membership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponseDto {
    private Long id;
    private String name;
    private Membership.MembershipType type;
    private Integer conversationLimit;
    private Integer durationDays;
    private Integer price;
    private Boolean isCustom;

    public static MembershipResponseDto from(Membership membership) {
        return MembershipResponseDto.builder()
                .id(membership.getId())
                .name(membership.getName())
                .type(membership.getType())
                .conversationLimit(membership.getConversationLimit())
                .durationDays(membership.getDurationDays())
                .price(membership.getPrice())
                .isCustom(membership.getIsCustom())
                .build();
    }
}