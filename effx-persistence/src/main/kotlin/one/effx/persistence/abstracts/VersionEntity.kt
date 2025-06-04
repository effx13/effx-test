package one.effx.persistence.abstracts

import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column

abstract class VersionEntity(
    @Version
    @Column("version")
    var version: Long = 0L,
) : MutableEntity() {
    abstract override fun getId(): Long?
}