package com.hackaction.sidul_restful_api.authentication

import com.hackaction.sidul_restful_api.core.enums.ResponseStatus
import com.hackaction.sidul_restful_api.core.enums.Role
import com.hackaction.sidul_restful_api.core.services.JwtService
import com.hackaction.sidul_restful_api.major.MajorRepository
import com.hackaction.sidul_restful_api.user.User
import com.hackaction.sidul_restful_api.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager,
    val majorRepository: MajorRepository
) {
    fun register(registerRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        // TODO optimize this two if so it should know if email or phone number already exist in single method call
        if (userRepository.findByEmail(registerRequest.email).isPresent) {
            return ResponseEntity(
                AuthenticationResponse(
                    status = ResponseStatus.FAIL, data = mapOf("token" to "email sudah terdaftar")
                ), HttpStatus.CONFLICT
            )
        }

        if (userRepository.findByPhoneNumber(registerRequest.phoneNumber).isPresent) {
            return ResponseEntity(
                AuthenticationResponse(
                    status = ResponseStatus.FAIL, data = mapOf("token" to "nomor handphone sudah terdaftar")
                ), HttpStatus.CONFLICT
            )
        }

        if (userRepository.findByFullname(registerRequest.fullname).isPresent) {
            return ResponseEntity(
                AuthenticationResponse(
                    status = ResponseStatus.FAIL, data = mapOf("token" to "nama lengkap sudah terdaftar")
                ), HttpStatus.CONFLICT
            )
        }

        if (userRepository.findByUsername(registerRequest.username).isPresent) {
            return ResponseEntity(
                AuthenticationResponse(
                    status = ResponseStatus.FAIL, data = mapOf("token" to "username sudah terdaftar")
                ), HttpStatus.CONFLICT
            )
        }

        val major = majorRepository.findByMajorName(registerRequest.major.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }).get();

        val user = User(
            fullname = registerRequest.fullname,
            username = registerRequest.username,
            email = registerRequest.email,
            phoneNumber = registerRequest.phoneNumber,
            dob = registerRequest.dob,
            // photoProfile = registerRequest.photoProfile.bytes,
            role = Role.valueOf(registerRequest.role),
            password = passwordEncoder.encode(registerRequest.password),
            major = major,
        )

        userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)

        return ResponseEntity(
            AuthenticationResponse(
                status = ResponseStatus.SUCCESS, data = mapOf("token" to jwtToken)
            ), HttpStatus.CREATED
        )
    }

    fun login(loginRequest: LoginRequest): ResponseEntity<AuthenticationResponse> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username, loginRequest.password
            )
        )

        val user = userRepository.findByUsername(loginRequest.username).orElseThrow()

        val jwtToken = jwtService.generateToken(user)

        return ResponseEntity(
            AuthenticationResponse(
                status = ResponseStatus.SUCCESS, data = mapOf("token" to jwtToken)
            ), HttpStatus.OK
        )
    }
}