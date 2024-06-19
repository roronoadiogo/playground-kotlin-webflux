package com.roronoadiogo.playground.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlaygroundWebFluxApplication

fun main(args: Array<String>) {
	runApplication<PlaygroundWebFluxApplication>(*args)
}
