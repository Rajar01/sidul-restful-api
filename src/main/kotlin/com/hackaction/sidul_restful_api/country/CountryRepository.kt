package com.hackaction.sidul_restful_api.country

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface CountryRepository : JpaRepository<Country, UUID> {
    fun findByCountryName(countryName: String): Optional<Country>
}