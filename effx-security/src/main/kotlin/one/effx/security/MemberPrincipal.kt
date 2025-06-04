package one.effx.security

import one.effx.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class MemberPrincipal(
    val member: Member,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()
    override fun getPassword() = member.passwordHash
    override fun getUsername() = member.email
    override fun isEnabled() = member.isActive
}