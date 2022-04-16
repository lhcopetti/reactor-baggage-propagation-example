package dev.copetti.baggage.kafkareactorotelexample

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.cloud.sleuth.SpanAndScope
import org.springframework.cloud.sleuth.Tracer
import org.springframework.cloud.sleuth.propagation.Propagator
import org.springframework.context.event.EventListener
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver

@Component
class KafkaEventListener(
    private val kafkaReceiver: KafkaReceiver<String, String>,

    private val tracer: Tracer,
    private val extractor: Propagator.Getter<ConsumerRecord<*, *>>,
    private val propagator: Propagator,

    private val webclientBuilder: WebClient.Builder
) {

    @EventListener(ApplicationReadyEvent::class)
    fun subscribeToTopic() {
        kafkaReceiver
            .receiveAutoAck()
            .flatMap { it }
            .flatMap { record ->
                val builder = propagator.extract(record, extractor)
                val childSpan = builder.name("on-message").start()
                val spanAndScope = SpanAndScope(childSpan, tracer.withSpan(childSpan))

                callPrintHeaders().doFinally { spanAndScope.close() }
            }
            .subscribe()
    }

    fun callPrintHeaders(): Mono<ResponseEntity<Void>> {
        return webclientBuilder
            .baseUrl("http://localhost:8080")
            .build()
            .get()
            .uri("/printheaders")
            .retrieve()
            .toBodilessEntity()
    }

}