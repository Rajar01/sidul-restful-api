package com.hackaction.sidul_restful_api.core.configs

import com.hackaction.sidul_restful_api.core.filters.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtAuthenticationFilter: JwtAuthenticationFilter, val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/api/v1/accounts/**").permitAll().anyRequest().authenticated()
        }.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf { csrfConfigurer -> csrfConfigurer.disable() }
            .cors { corsConfigurer -> corsConfigurer.disable() }
            .authenticationProvider(authenticationProvider).addFilterBefore(
                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java
            )

        return httpSecurity.build()
    }
}