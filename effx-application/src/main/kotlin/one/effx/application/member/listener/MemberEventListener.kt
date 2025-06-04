package one.effx.application.member.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import one.effx.domain.member.event.MemberCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MemberEventListener {
    @EventListener(MemberCreatedEvent::class)
    fun onMemberCreated(event: MemberCreatedEvent) {
        log.info { "새로운 회원이 생성되었습니다. ID: ${event.id}, 이름: ${event.name}, 이메일: ${event.email}, 역할: ${event.role}" }
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}