package com.hackaction.sidul_restful_api.authentication

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/accounts")
class AuthenticationController(val authenticationService: AuthenticationService) {

    @PostMapping(path = ["/register"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun register(@ModelAttribute registerRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.register(registerRequest)

        return response
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.login(loginRequest)

        return response
    }
}