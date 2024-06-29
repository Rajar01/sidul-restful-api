package com.hackaction.sidul_restful_api.major

import com.hackaction.sidul_restful_api.user.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "major")
class Major(
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "major_id") val id: UUID? = null,
    @Column(name = "major_name") val majorName: String,

    @OneToMany(mappedBy = "major")  val users: Collection<User>? = null
) {

}
