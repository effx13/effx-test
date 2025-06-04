package one.effx.persistence.abstracts

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

abstract class MutableEntity : Persistable<Long> {
    @CreatedDate
    @Column("created_at")
    lateinit var createdAt: LocalDateTime
        protected set

    @LastModifiedDate
    @Column("modified_at")
    lateinit var modifiedAt: LocalDateTime
        protected set

    abstract override fun getId(): Long?
}