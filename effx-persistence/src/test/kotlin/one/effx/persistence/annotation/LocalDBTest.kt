package one.effx.persistence.annotation

import one.effx.persistence.config.DatabaseConfig
import org.junit.jupiter.api.Tag
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@Tag("local-test")
@ActiveProfiles("local")
@EnableAutoConfiguration
@SpringBootTest(classes = [DatabaseConfig::class], properties = ["spring.config.location=classpath:application-db.yml"])
@Target(AnnotationTarget.CLASS)
@Retention(
    AnnotationRetention.RUNTIME,
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
annotation class LocalDBTest