package com.example.activemq

import mu.KotlinLogging
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component
import java.util.*
import javax.jms.Message

@Component
class ActiveMQConsumer(val jmsTemplate: JmsTemplate) {
	companion object {
		private val logger = KotlinLogging.logger {}
	}

	@JmsListener(destination = "\${activemq.target.queue}")
	fun consumeQueue1(message: MyData) {
		logger.info("consumeQueue1 $message")
	}

	@JmsListener(destination = "\${activemq.target.queue}")
	fun consumeQueue2(message: MyData) {
		logger.info("consumeQueue2 $message")
	}


	@JmsListener(destination = "\${activemq.target.topic}", containerFactory = "jmsTopicContainerFactory")
	fun consumeTopic1(message: MyData) {
		logger.info("consumeTopic1  $message")
	}

	@JmsListener(destination = "\${activemq.target.topic}", containerFactory = "jmsTopicContainerFactory")
	fun consumeTopic2(message: MyData) {
		logger.info("consumeTopic2  $message")
	}

	/**
	 * Tick mit Topic
	 */
	@JmsListener(destination = "\${activemq.target.rpc}", containerFactory = "jmsTopicContainerFactory")
	fun consumeRPC(message: Message) {
		logger.info("consumeRPC $message")
		jmsTemplate.convertAndSend(message.jmsReplyTo, Date().toString())
	}
}