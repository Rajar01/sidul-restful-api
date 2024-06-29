package com.hackaction.sidul_restful_api

import com.hackaction.sidul_restful_api.major.Major
import com.hackaction.sidul_restful_api.major.MajorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(
    @Autowired val majorRepository: MajorRepository

) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val majors = listOf<Major>(Major(majorName = "Teknik Informatika"), Major(majorName = "Akutansi"))

        majorRepository.saveAll(majors)
    }
}