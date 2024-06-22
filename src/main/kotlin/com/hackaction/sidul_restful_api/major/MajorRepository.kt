package com.hackaction.sidul_restful_api.major

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MajorRepository : JpaRepository<Major, UUID> {
    fun findByMajorName(majorName: String): Optional<Major>
}