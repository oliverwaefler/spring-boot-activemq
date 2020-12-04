package com.example.activemq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ActiveMQApplication

fun main(args: Array<String>) {
	runApplication<ActiveMQApplication>(*args)
}
