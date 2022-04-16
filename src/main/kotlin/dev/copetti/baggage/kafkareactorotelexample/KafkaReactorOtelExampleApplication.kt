package dev.copetti.baggage.kafkareactorotelexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaReactorOtelExampleApplication

fun main(args: Array<String>) {
	runApplication<KafkaReactorOtelExampleApplication>(*args)
}
