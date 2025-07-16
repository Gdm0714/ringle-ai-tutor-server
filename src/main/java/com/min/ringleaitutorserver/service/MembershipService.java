package com.min.ringleaitutorserver.service;

import com.min.ringleaitutorserver.dto.*;
import com.min.ringleaitutorserver.entity.Membership;
import com.min.ringleaitutorserver.entity.UserMembership;
import com.min.ringleaitutorserver.exception.BusinessException;
import com.min.ringleaitutorserver.exception.MembershipErrorCode;
import com.min.ringleaitutorserver.repository.MembershipRepository;
import com.min.ringleaitutorserver.repository.UserMembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final PaymentService paymentService;

    // 사용자 멤버십 조회
    @Transactional(readOnly = true)
    public UserMembershipResponseDto getUserMembership(Long userId) {
        UserMembership userMembership = userMembershipRepository
                .findFirstByUserIdAndStatusOrderByExpiryDateDesc(userId, "active")
                .orElse(null);

        if (userMembership == null) {
            return null;
        }

        if (userMembership.isExpired()) {
            userMembership.setStatus("expired");
            userMembershipRepository.save(userMembership);
            return null;
        }

        return UserMembershipResponseDto.from(userMembership);
    }

    // 대화 횟수 사용
    public boolean useConversation(Long userId) {
        UserMembership userMembership = userMembershipRepository
                .findFirstByUserIdAndStatusOrderByExpiryDateDesc(userId, "active")
                .orElse(null);

        if (userMembership == null) {
            log.warn("No active membership found for user: {}", userId);
            return false;
        }

        if (userMembership.isExpired()) {
            log.info("Membership expired for user: {}", userId);
            userMembership.setStatus("expired");
            userMembershipRepository.save(userMembership);
            return false;
        }

        if (!userMembership.canUseConversation()) {
            log.warn("User {} cannot use conversation. Used: {}, Limit: {}", 
                userId, userMembership.getConversationUsed(), 
                userMembership.getMembership().getConversationLimit());
            return false;
        }

        userMembership.setConversationUsed(userMembership.getConversationUsed() + 1);
        userMembershipRepository.save(userMembership);
        log.info("Conversation used for user: {}. Used: {}", 
            userId, userMembership.getConversationUsed());
        return true;
    }


    // 멤버십 생성
    public MembershipResponseDto createMembership(MembershipCreateRequestDto request) {
        Membership membership = request.toEntity();
        Membership saved = membershipRepository.save(membership);
        return MembershipResponseDto.from(saved);
    }

    // 커스텀 멤버십 생성
    public MembershipResponseDto createCustomMembership(MembershipCreateRequestDto request) {
        Membership membership = request.toCustomEntity();
        Membership saved = membershipRepository.save(membership);
        return MembershipResponseDto.from(saved);
    }

    // 모든 멤버십 조회
    @Transactional(readOnly = true)
    public List<MembershipResponseDto> getAllMemberships() {
        return membershipRepository.findAll().stream()
                .map(MembershipResponseDto::from)
                .collect(Collectors.toList());
    }

    // 타입별 멤버십 조회
    @Transactional(readOnly = true)
    public List<MembershipResponseDto> getMembershipsByType(Membership.MembershipType type) {
        return membershipRepository.findByType(type).stream()
                .map(MembershipResponseDto::from)
                .collect(Collectors.toList());
    }

    // 사용자에게 멤버십 할당
    public UserMembershipResponseDto assignMembershipToUser(AdminMembershipAssignRequestDto request) {
        Membership membership = membershipRepository.findById(request.getMembershipId())
                .orElseThrow(() -> new BusinessException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND));

        UserMembership userMembership = UserMembership.of(request.getUserId(), membership);
        UserMembership saved = userMembershipRepository.save(userMembership);
        return UserMembershipResponseDto.from(saved);
    }

    // 멤버십 삭제
    public void deleteMembership(Long membershipId) {
        if (!membershipRepository.existsById(membershipId)) {
            throw new BusinessException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND);
        }
        
        // 해당 멤버십을 참조하는 모든 UserMembership을 먼저 삭제
        userMembershipRepository.deleteByMembershipId(membershipId);
        
        // 이제 멤버십 삭제
        membershipRepository.deleteById(membershipId);
    }

    // 사용자 멤버십 삭제
    public void deleteUserMembership(Long userId) {
        List<UserMembership> userMemberships = userMembershipRepository.findByUserIdAndStatus(userId, "active");
        userMemberships.forEach(um -> {
            um.setStatus("expired");
            userMembershipRepository.save(um);
        });
    }

    // 결제 처리
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        Membership membership = membershipRepository.findById(request.getMembershipId())
                .orElseThrow(() -> new BusinessException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND));

        if (membership.getPrice() == null || membership.getPrice() <= 0) {
            return PaymentResponseDto.failure("구매할 수 없는 멤버십입니다");
        }

        PaymentService.PaymentResult result = paymentService.processPayment(
                request.getPaymentMethod(),
                request.getPaymentToken(),
                membership.getPrice()
        );

        if (!result.isSuccess()) {
            return PaymentResponseDto.failure(result.getErrorMessage());
        }

        UserMembership userMembership = UserMembership.of(request.getUserId(), membership);
        UserMembership saved = userMembershipRepository.save(userMembership);
        UserMembershipResponseDto membershipDto = UserMembershipResponseDto.from(saved);

        return PaymentResponseDto.success(result.getPaymentId(), membership.getPrice(), membershipDto);
    }
}
