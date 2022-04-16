package dev.copetti.baggage.kafkareactorotelexample

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

@Component
class KafkaEventPublisher(
    private val kafkaSender: KafkaSender<String, String>
) {
    private val log = LoggerFactory.getLogger(KafkaEventPublisher::class.java.simpleName)

    fun sendEvent(): Flux<SenderResult<String>> {
        val record = SenderRecord
            .create<String, String, String>("test", null, null, null, "the-value", null)
        return kafkaSender.send(Mono.just(record))
            .doOnComplete { log.info("Published to topic: test") }
    }
}