package com.hackaction.sidul_restful_api.authentication

import org.springframework.web.bind.annotation.BindParam
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class RegisterRequest(
    val fullname: String,
    val username: String,
    val password: String,
    val email: String,
    @BindParam("phone_number") val phoneNumber: String,
    // The date format is ISO, yyyy-MM-dd
    val dob: LocalDate,
    @BindParam("country_name") val countryName: String,
    @BindParam("photo_profile") val photoProfile: MultipartFile,
    val role: String,
    val majors: Collection<String>
)
