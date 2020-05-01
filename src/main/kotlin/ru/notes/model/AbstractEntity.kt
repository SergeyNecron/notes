package ru.notes.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    val id: Long = -1

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "create_date")
    val createDate: LocalDateTime = LocalDateTime.now()

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "modify_date")
    var modifyDate: LocalDateTime = LocalDateTime.now()
}