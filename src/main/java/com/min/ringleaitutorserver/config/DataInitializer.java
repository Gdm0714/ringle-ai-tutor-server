package com.min.ringleaitutorserver.config;

import com.min.ringleaitutorserver.entity.Membership;
import com.min.ringleaitutorserver.entity.UserMembership;
import com.min.ringleaitutorserver.repository.MembershipRepository;
import com.min.ringleaitutorserver.repository.UserMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MembershipRepository membershipRepository;
    private final UserMembershipRepository userMembershipRepository;

    @Override
    public void run(String... args) {
        if (membershipRepository.count() > 0) {
            System.out.println("멤버십 데이터가 이미 존재합니다. 현재 멤버십 수: " + membershipRepository.count());
            return;
        }
        
        System.out.println("초기 멤버십 데이터를 생성합니다...");

        Membership basic = membershipRepository.save(Membership.of(
                "베이직 멤버십", 
                Membership.MembershipType.B2C,
                20, 
                30, 
                29000
        ));

        Membership premium = membershipRepository.save(Membership.of(
                "프리미엄 멤버십",
                Membership.MembershipType.B2C,
                50,
                60,
                59000
        ));

        Membership unlimited = membershipRepository.save(Membership.of(
                "무제한 멤버십",
                Membership.MembershipType.B2C,
                null,
                90,
                99000
        ));

        Membership corporate = membershipRepository.save(Membership.of(
                "기업용 멤버십",
                Membership.MembershipType.B2B,
                100,
                365,
                500000
        ));

        UserMembership userMembership = UserMembership.of(1L, basic);
        userMembershipRepository.save(userMembership);
        
        System.out.println("초기 데이터 생성 완료! 총 " + membershipRepository.count() + "개의 멤버십이 생성되었습니다.");
    }
}