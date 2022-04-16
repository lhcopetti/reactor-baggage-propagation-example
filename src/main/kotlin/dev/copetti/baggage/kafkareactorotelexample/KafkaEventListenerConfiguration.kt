package dev.copetti.baggage.kafkareactorotelexample

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.cloud.sleuth.instrument.kafka.TracingKafkaConsumerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaEventListenerConfiguration {

    @Bean
    fun kafkaReceiverConfiguration(
        tracingKafkaConsumerFactory: TracingKafkaConsumerFactory,
        kafkaProperties: KafkaProperties
    ): KafkaReceiver<String, String> {
        val props = kafkaProperties.buildConsumerProperties()
        val options = ReceiverOptions.create<String, String>(props)
            .subscription(listOf("test"))
        return KafkaReceiver.create(tracingKafkaConsumerFactory, options)
    }
}