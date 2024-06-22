package com.hackaction.sidul_restful_api.country

import com.hackaction.sidul_restful_api.user.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "country")
class Country(
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "country_id") val id: UUID? = null,
    @Column(name = "country_name") val countryName: String,
    @OneToMany(mappedBy = "country") val user: Collection<User>? = null
)