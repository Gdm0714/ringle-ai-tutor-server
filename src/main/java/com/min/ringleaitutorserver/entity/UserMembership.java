package com.min.ringleaitutorserver.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    @NotNull
    @Builder.Default
    private Integer conversationUsed = 0;


    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime expiryDate;

    @NotNull
    @Builder.Default
    private String status = "active";

    @PrePersist
    public void prePersist() {
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }
        if (expiryDate == null && membership != null) {
            expiryDate = startDate.plusDays(membership.getDurationDays());
        }
    }

    public static UserMembership of(Long userId, Membership membership) {
        LocalDateTime now = LocalDateTime.now();
        return UserMembership.builder()
                .userId(userId)
                .membership(membership)
                .startDate(now)
                .expiryDate(now.plusDays(membership.getDurationDays()))
                .build();
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    public boolean canUseConversation() {
        if (isExpired() || !"active".equals(status)) {
            return false;
        }
        Integer limit = membership.getConversationLimit();
        return limit == null || conversationUsed < limit;
    }

    public UserMembership useConversation() {
        if (!canUseConversation()) {
            throw new IllegalStateException("대화 기능을 사용할 수 없습니다");
        }
        return this.toBuilder()
                .conversationUsed(conversationUsed + 1)
                .build();
    }

    public UserMembership expire() {
        return this.toBuilder()
                .status("expired")
                .build();
    }
}