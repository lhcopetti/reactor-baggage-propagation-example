package dev.copetti.baggage.kafkareactorotelexample

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping
class TriggerKafkaListenerController(
    private val kafkaEventPublisher: KafkaEventPublisher
) {

    private val log = LoggerFactory.getLogger(TriggerKafkaListenerController::class.java)

    @GetMapping
    fun triggerKafkaSending(): Mono<Void> {
        return kafkaEventPublisher.sendEvent()
            .then()
    }

    @GetMapping
    @RequestMapping("/printheaders")
    fun triggerKafkaSending(
        @RequestHeader headers: Map<String, String>
    ): Mono<Void> {
        headers.forEach { (t, u) -> log.info("Header: $t -> $u") }
        return Mono.empty()
    }
}