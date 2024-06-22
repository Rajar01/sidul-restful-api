package com.hackaction.sidul_restful_api.user

import com.hackaction.sidul_restful_api.core.enums.Role
import com.hackaction.sidul_restful_api.country.Country
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
    private val password: String,
    @Column(unique = true) val email: String,
    @Column(name = "phone_number", unique = true) val phoneNumber: String,
    val dob: LocalDate,
    @ManyToOne(cascade = [CascadeType.ALL]) @JoinColumn(name = "country_id") val country: Country,
    @Lob @Basic(fetch = FetchType.LAZY) @Column(
        name = "photo_profile", columnDefinition = "MEDIUMBLOB"
    ) val photoProfile: ByteArray,
    @Enumerated(EnumType.STRING) val role: Role,
    @ManyToMany(cascade = [CascadeType.ALL]) @JoinTable(
        name = "user_major",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "major_id")]
    ) val majors: Collection<Major>
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