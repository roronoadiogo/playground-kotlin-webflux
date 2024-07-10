package com.roronoadiogo.playground.webflux.infrastructure.adapter.output.persistence

import com.roronoadiogo.playground.webflux.domain.exception.DatabaseOperationException
import com.roronoadiogo.playground.webflux.domain.model.Product
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.test.StepVerifier
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
class ReactiveProductRepositoryTest {

    @Autowired
    lateinit var repository: ReactiveProductRepository

    companion object {
        val log = LoggerFactory.getLogger(ReactiveProductRepositoryTest::class.java)
    }

    @Test
    fun `find all success`() {
        repository.findAll()
            .doOnNext { product -> log.info("{}", product) }
            .`as` { StepVerifier.create(it) }
            .expectNextCount(20)
            .verifyComplete()
    }

    @Test
    fun `find byId success`() {
        repository.findById(7)
            .doOnNext { product -> log.info("{}", product) }
            .`as` { StepVerifier.create(it) }
            .assertNext { product -> assertEquals("Coffee Maker", product.name) }
            .expectComplete()
            .verify()
    }

    @Test
    fun `create product success`() {
        repository.save(Product.create("Test", BigDecimal(10)))
            .doOnNext { product -> log.info("{}", product) }
            .`as` { StepVerifier.create(it) }
            .assertNext { product -> checkNotNull(product.id) }
            .expectComplete()
            .verify()
    }

    @Test
    fun `delete product success`() {
        val product = Product(21L, "Test Product", BigDecimal(10))

        repository.save(product).block()
        val result = repository.deleteById(product.id)

        StepVerifier.create(result)
            .verifyComplete()

        StepVerifier.create(repository.findById(product.id))
            .expectError(DatabaseOperationException::class.java)
            .verify()
    }
}