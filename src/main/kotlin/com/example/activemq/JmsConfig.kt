package com.example.activemq

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory


@Configuration
@EnableJms
class JmsConfig(@Value("\${activemq.broker-url}") var brokerUrl: String = "tcp://localhost:61616") {

	@Bean
	fun connectionFactory(): ActiveMQConnectionFactory {
		val activeMQConnectionFactory = ActiveMQConnectionFactory()
		activeMQConnectionFactory.brokerURL = brokerUrl
		return activeMQConnectionFactory
	}

	@Bean
	fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory? {
		val factory = DefaultJmsListenerContainerFactory()
		factory.setConnectionFactory(connectionFactory())
		return factory
	}

	@Bean
	fun jmsTopicContainerFactory(): DefaultJmsListenerContainerFactory? {
		val factory = DefaultJmsListenerContainerFactory()
		factory.setConnectionFactory(connectionFactory())
		factory.setPubSubDomain(true)
		return factory
	}
}