package one.effx.security.provider

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {
    @Value("\${security.jwt.secret.access-token}")
    private lateinit var accessTokenSecret: String

    @Value("\${security.jwt.secret.refresh-token}")
    private lateinit var refreshTokenSecret: String

    private val accessTokenKey by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret)) }
    private val refreshTokenKey by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecret)) }

    fun generateAccessToken(auth: Authentication): String {
        return generateToken(
            auth,
            accessTokenKey,
            ACCESS_TOKEN_EXPIRATION_TIME
        )
    }

    fun generateRefreshToken(auth: Authentication): String {
        return generateToken(
            auth,
            refreshTokenKey,
            REFRESH_TOKEN_EXPIRATION_TIME
        )
    }

    fun refreshAccessToken(token: String): String {
        return generateAccessToken(getRefreshTokenAuthentication(token))
    }

    private fun generateToken(
        auth: Authentication,
        key: SecretKey,
        expireTime: Long
    ): String {
        val now = Date()
        val expirationTime = Date(now.time + expireTime)

        return Jwts.builder()
            .subject(auth.name)
            .claim(KEY_ROLE, auth.authorities.firstOrNull()?.authority ?: "USER")
            .issuedAt(now)
            .expiration(expirationTime)
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = parseClaims(token, accessTokenKey)

        val authorities = getAuthorities(claims)

        val principal = User(
            claims.subject,
            "",
            authorities,
        )
        return UsernamePasswordAuthenticationToken(
            principal,
            token,
            authorities,
        )
    }

    private fun getRefreshTokenAuthentication(token: String): Authentication {
        val claims = parseClaims(token, refreshTokenKey)

        val authorities = getAuthorities(claims)

        val principal = User(
            claims.subject,
            "",
            authorities,
        )
        return UsernamePasswordAuthenticationToken(
            principal,
            token,
            authorities,
        )
    }

    private fun getAuthorities(claims: Claims): List<SimpleGrantedAuthority> {
        return listOf(
            SimpleGrantedAuthority(
                claims[KEY_ROLE].toString()
            )
        )
    }

    private fun parseClaims(token: String, key: SecretKey): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    companion object {
        const val ACCESS_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 30 // 30 minutes
        const val REFRESH_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 60 * 24 * 7 // 7 days
        const val KEY_ROLE = "role"
    }
}