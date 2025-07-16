package com.min.ringleaitutorserver.repository;

import com.min.ringleaitutorserver.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    Optional<UserMembership> findFirstByUserIdAndStatusOrderByExpiryDateDesc(Long userId, String status);
    List<UserMembership> findByUserIdAndStatus(Long userId, String status);
    void deleteByMembershipId(Long membershipId);
}
