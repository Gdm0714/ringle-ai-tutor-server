package com.min.ringleaitutorserver.controller;

import com.min.ringleaitutorserver.dto.*;
import com.min.ringleaitutorserver.entity.Membership;
import com.min.ringleaitutorserver.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserMembershipResponseDto> getUserMembership(@PathVariable Long userId) {
        UserMembershipResponseDto membership = membershipService.getUserMembership(userId);
        if (membership == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(membership);
    }

    @GetMapping
    public ResponseEntity<List<MembershipResponseDto>> getAllMemberships() {
        List<MembershipResponseDto> memberships = membershipService.getAllMemberships();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MembershipResponseDto>> getMembershipsByType(@PathVariable Membership.MembershipType type) {
        List<MembershipResponseDto> memberships = membershipService.getMembershipsByType(type);
        return ResponseEntity.ok(memberships);
    }

    @PostMapping
    public ResponseEntity<MembershipResponseDto> createMembership(@Valid @RequestBody MembershipCreateRequestDto request) {
        MembershipResponseDto membership = membershipService.createMembership(request);
        return ResponseEntity.ok(membership);
    }

    @PostMapping("/custom")
    public ResponseEntity<MembershipResponseDto> createCustomMembership(@Valid @RequestBody MembershipCreateRequestDto request) {
        MembershipResponseDto membership = membershipService.createCustomMembership(request);
        return ResponseEntity.ok(membership);
    }

    @PostMapping("/assign")
    public ResponseEntity<UserMembershipResponseDto> assignMembershipToUser(@Valid @RequestBody AdminMembershipAssignRequestDto request) {
        UserMembershipResponseDto userMembership = membershipService.assignMembershipToUser(request);
        return ResponseEntity.ok(userMembership);
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<Void> deleteMembership(@PathVariable Long membershipId) {
        membershipService.deleteMembership(membershipId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserMembership(@PathVariable Long userId) {
        membershipService.deleteUserMembership(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponseDto> processPayment(@Valid @RequestBody PaymentRequestDto request) {
        PaymentResponseDto result = membershipService.processPayment(request);
        return ResponseEntity.ok(result);
    }
}
