package com.example.activemq

import com.beust.klaxon.Klaxon
import mu.KotlinLogging
import org.apache.activemq.command.ActiveMQTextMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class RestController(val jmsTemplate: JmsTemplate,
					 @Value("\${activemq.target.queue}") private var queueName: String = "job_queue",
					 @Value("\${activemq.target.topic}") private var topicName: String = "pub_sub_topic",
					 @Value("\${activemq.target.rpc}") private var rpcName: String = "rpc_topic") {
	companion object {
		private val logger = KotlinLogging.logger {}
	}

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/queue"])
	fun postQueue(@RequestBody myData: MyData) {
		logger.debug { "Sending $myData to quque - $queueName" }
		jmsTemplate.isPubSubDomain = false
		jmsTemplate.convertAndSend(queueName, Klaxon().toJsonString(myData))
		logger.info { "Sent $myData to queue - $queueName" }

	}

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/topic"])
	fun postTopic(@RequestBody myData: MyData) {
		logger.debug { "Sending $myData to topic - $topicName" }
		jmsTemplate.isPubSubDomain = true
		jmsTemplate.convertAndSend(topicName, Klaxon().toJsonString(myData))
		logger.info { "Sent $myData to topic - $topicName" }
	}

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/rpc"])
	fun postRPC(@RequestBody myData: MyData) {
		logger.debug { "Sending $myData to topic - $rpcName" }
		jmsTemplate.isPubSubDomain = true
		val result = jmsTemplate.sendAndReceive(rpcName) { session ->
			val message = session.createTextMessage(Klaxon().toJsonString(myData))
			message.jmsReplyTo = session.createTemporaryQueue()
			message
		}
		logger.info { "Sent $myData to $rpcName and received ${(result as ActiveMQTextMessage).text}" }
	}
}