package one.effx.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import one.effx.security.filter.AuthExceptionFilter
import one.effx.security.filter.JwtTokenAuthenticationFilter
import one.effx.security.handler.CustomServerAccessDeniedHandler
import one.effx.security.handler.CustomServerAuthenticationEntryPoint
import one.effx.security.manager.EmailPasswordAuthenticationManager
import one.effx.security.provider.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@ComponentScan(basePackages = ["one.effx.security"])
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    private val customServerAccessDeniedHandler: CustomServerAccessDeniedHandler,
    private val customServerAuthenticationEntryPoint: CustomServerAuthenticationEntryPoint,
    private val reactiveUserDetailsService: ReactiveUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity
    ): SecurityWebFilterChain {
        return http {
            csrf {
                disable()
            }
            cors {
                disable()
            }
            httpBasic {
                disable()
            }
            formLogin {
                disable()
            }
            logout {
                disable()
            }
            headers {
                frameOptions {
                    disable()
                }
            }
            securityContextRepository = NoOpServerSecurityContextRepository.getInstance()
            exceptionHandling {
                accessDeniedHandler = customServerAccessDeniedHandler
                authenticationEntryPoint = customServerAuthenticationEntryPoint
            }
            authorizeExchange {
                authorize("/v3/api-docs/**", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/swagger-resources/**", permitAll)
                authorize("/h2-console/**", permitAll)
                authorize("/v1/auth/**", permitAll)

                authorize(anyExchange, authenticated)
            }
            addFilterAfter(
                AuthExceptionFilter(objectMapper),
                SecurityWebFiltersOrder.LAST
            )
            addFilterAfter(
                JwtTokenAuthenticationFilter(jwtTokenProvider),
                SecurityWebFiltersOrder.AUTHENTICATION
            )
        }
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        return EmailPasswordAuthenticationManager(reactiveUserDetailsService, passwordEncoder())
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}