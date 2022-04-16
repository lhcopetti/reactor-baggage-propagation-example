package dev.copetti.baggage.kafkareactorotelexample

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.cloud.sleuth.instrument.kafka.TracingKafkaProducerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaEventPublisherConfiguration {

    @Bean
    fun getKafkaSender(
        producerFactory: TracingKafkaProducerFactory,
        kafkaProperties: KafkaProperties
    ): KafkaSender<String, String> {
        val props = kafkaProperties.buildProducerProperties()
        val options = SenderOptions.create<String, String>(props)
        return KafkaSender.create(producerFactory,options)
    }
}