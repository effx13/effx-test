package one.effx.api.member.controller

import one.effx.api.annotation.MemberPrincipal
import one.effx.domain.member.Member
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController {
    @GetMapping("/v1/member/profile")
    suspend fun getProfile(
        @MemberPrincipal
        member: Member
    ): ResponseEntity<Member> {
        return ResponseEntity.ok(member)
    }
}