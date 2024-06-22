package com.hackaction.sidul_restful_api.authentication

import com.hackaction.sidul_restful_api.core.enums.ResponseStatus
import com.hackaction.sidul_restful_api.core.enums.Role
import com.hackaction.sidul_restful_api.core.services.JwtService
import com.hackaction.sidul_restful_api.country.Country
import com.hackaction.sidul_restful_api.country.CountryRepository
import com.hackaction.sidul_restful_api.major.Major
import com.hackaction.sidul_restful_api.major.MajorRepository
import com.hackaction.sidul_restful_api.user.User
import com.hackaction.sidul_restful_api.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager,
    val countryRepository: CountryRepository,
    val majorRepository: MajorRepository
) {
    fun register(registerRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        // TODO optimize this two if so it should know if email or phone number already exist in single method call
        if (userRepository.findByEmail(registerRequest.email).isPresent) {
            return ResponseEntity(
                AuthenticationResponse(
                    status = ResponseStatus.FAIL, data = mapOf("token" to "email is already exist")
                ), HttpStatus.CONFLICT
            )
        }

        if (userRepository.findByPhoneNumber(registerRequest.phoneNumber).isPresent) {
            return ResponseEntity(
                AuthenticationResponse(
                    status = ResponseStatus.FAIL, data = mapOf("token" to "phone number is already exist")
                ), HttpStatus.CONFLICT
            )
        }

        val country = countryRepository.findByCountryName(registerRequest.countryName)
            .getOrElse { countryRepository.save(Country(countryName = registerRequest.countryName)) }

        val majors = mutableListOf<Major>()
        registerRequest.majors.forEach {
            val major = majorRepository.findByMajorName(it).getOrElse { majorRepository.save(Major(majorName = it)) }
            majors.add(major)
        }

        val user = User(
            fullname = registerRequest.fullname,
            username = registerRequest.username,
            email = registerRequest.email,
            phoneNumber = registerRequest.phoneNumber,
            dob = registerRequest.dob,
            country = country,
            role = Role.valueOf(registerRequest.role),
            majors = majors,
            password = passwordEncoder.encode(registerRequest.password),
            photoProfile = registerRequest.photoProfile.bytes
        )

        userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)

        return ResponseEntity(
            AuthenticationResponse(
                status = ResponseStatus.SUCCESS, data = mapOf("token" to jwtToken)
            ), HttpStatus.OK
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