package one.effx.persistence.member.adapter

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import one.effx.domain.member.Member
import one.effx.domain.member.vo.MemberRole
import one.effx.persistence.DataSweepUtils
import one.effx.persistence.annotation.LocalDBTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator

@LocalDBTest
class MemberAdapterTest(
    private val databaseClient: DatabaseClient,
    private val transactionalOperator: TransactionalOperator,
    private val memberAdapter: MemberAdapter
) : FunSpec({
    fun stubMember(
        id: Long,
        name: String? = null,
        email: String? = null,
        passwordHash: String? = null,
        role: MemberRole? = null,
        isActive: Boolean? = null
    ): Member {
        return Member(
            id = id,
            name = name ?: "홍길동",
            email = email ?: "test@example.com",
            passwordHash = passwordHash ?: "hashedPassword123",
            role = role ?: MemberRole.ROLE_USER,
            isActive = isActive ?: true
        )
    }

    beforeTest {
        runBlocking {
            DataSweepUtils(
                transactionalOperator = transactionalOperator,
                databaseClient = databaseClient
            ).sweep()
        }
    }

    test("새로운 회원을 저장할 수 있어야 한다.") {
        // given
        val member = stubMember(id = 0L)

        // when
        val savedMember = memberAdapter.save(member)

        // then
        savedMember.name shouldBe member.name
        savedMember.email shouldBe member.email
        savedMember.passwordHash shouldBe member.passwordHash
        savedMember.role shouldBe member.role
        savedMember.isActive shouldBe member.isActive
    }

    test("기존 회원 정보를 업데이트할 수 있어야 한다.") {
        // given
        val existingMember = stubMember(id = 0L)
        val savedFirstMember = memberAdapter.save(existingMember)

        // when
        val updatedMember = existingMember.copy(id = savedFirstMember.id, name = "김철수")
        val savedMember = memberAdapter.save(updatedMember)

        // then
        savedMember.name shouldBe "김철수"
    }

    test("ID로 회원을 조회할 수 있어야 한다.") {
        // given
        val member = stubMember(id = 0L)

        val savedMember = memberAdapter.save(member)

        // when
        val foundMember = memberAdapter.findById(savedMember.id)

        // then
        foundMember.name shouldBe member.name
        foundMember.email shouldBe member.email
    }

    test("이메일로 회원을 조회할 수 있어야 한다.") {
        // given
        val member = stubMember(id = 0L)

        memberAdapter.save(member)

        // when
        val foundMember = memberAdapter.findByEmail(member.email)

        // then
        foundMember?.name shouldBe member.name
        foundMember?.email shouldBe member.email
    }
})
