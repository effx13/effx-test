package one.effx.domain.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["one.effx.api"]
)
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}