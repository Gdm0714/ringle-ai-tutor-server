package com.min.ringleaitutorserver.dto;

import com.min.ringleaitutorserver.entity.UserMembership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMembershipResponseDto {
    private Long id;
    private Long userId;
    private Integer conversationUsed;
    private Integer conversationRemaining;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private String status;
    private Boolean canUseConversation;
    private MembershipResponseDto membership;

    public static UserMembershipResponseDto from(UserMembership userMembership) {
        Integer convLimit = userMembership.getMembership().getConversationLimit();

        return UserMembershipResponseDto.builder()
                .id(userMembership.getId())
                .userId(userMembership.getUserId())
                .conversationUsed(userMembership.getConversationUsed())
                .conversationRemaining(convLimit == null ? -1 : convLimit - userMembership.getConversationUsed())
                .startDate(userMembership.getStartDate())
                .expiryDate(userMembership.getExpiryDate())
                .status(userMembership.getStatus())
                .canUseConversation(userMembership.canUseConversation())
                .membership(MembershipResponseDto.from(userMembership.getMembership()))
                .build();
    }
}