package com.hackaction.sidul_restful_api.authentication

import org.springframework.web.bind.annotation.BindParam
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class RegisterRequest(
    val fullname: String,
    val username: String,
    val email: String,
    @BindParam("phone_number") val phoneNumber: String,
    val dob: LocalDate,
    // @BindParam("photo_profile") val photoProfile: MultipartFile,
    val role: String,
    val password: String,
    // The date format is ISO, yyyy-MM-dd
    val major: String
)
