package com.hackaction.sidul_restful_api.user

import com.hackaction.sidul_restful_api.core.enums.Role
import com.hackaction.sidul_restful_api.major.Major
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.util.*


@Entity
@Table(name = "user")
class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "user_id") val id: UUID? = null,
    @Column(unique = true) val fullname: String,
    @Column(unique = true) private val username: String,
    @Column(unique = true) val email: String,
    @Column(name = "phone_number", unique = true) val phoneNumber: String,
    val dob: LocalDate,
    /*@Lob @Basic(fetch = FetchType.LAZY) @Column(
        name = "photo_profile", columnDefinition = "MEDIUMBLOB"
    ) val photoProfile: ByteArray,*/
    @Enumerated(EnumType.STRING) val role: Role,
    private val password: String,
    @ManyToOne(cascade = [CascadeType.ALL]) @JoinColumn(name = "major_id") val major: Major,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return password;
    }

    override fun getUsername(): String {
        return username;
    }
}