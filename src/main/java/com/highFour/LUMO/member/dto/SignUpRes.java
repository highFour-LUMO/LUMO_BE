//package com.highFour.LUMO.member.dto;
//
//import com.highFour.LUMO.member.entity.Member;
//import com.highFour.LUMO.member.entity.SocialType;
//import lombok.Builder;
//
//@Builder
//public record SignUpRes(Long memberId,
//                        String name,
//                        String email,
//                        String socialId,
//                        SocialType socialType,
//                        String role,
//                        String accessToken,
//                        String refreshToken){
//    public static SignUpRes fromEntity(Member member, JwtToken jwtToken) {
//        return SignUpRes.builder()
//                .memberId(member.getId())
//                .name(member.getName())
//                .email(member.getEmail())
//                .socialId(member.getSocialId())
//                .socialType(member.getSocialType())
//                .accessToken(jwtToken.accessToken())
//                .refreshToken(jwtToken.refreshToken())
//                .build();
//    }
//}
