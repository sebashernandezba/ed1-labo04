package ed.lab.ed1labo04.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E01ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void testCreateProduct() {
        String json = """
            {
                "name": "Test Product",
                "price": 10.5
            }
        """;

        webTestClient.post()
                .uri("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Test Product")
                .jsonPath("$.price").isEqualTo(10.5)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.quantity").isEqualTo(0);
    }

    @Test
    @Order(3)
    void testCreateProductWithPriceEqualToZero() {
        String json = """
            {
                "name": "Test Product",
                "price": 0.0
            }
        """;

        webTestClient.post()
                .uri("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testCreateProductWithNegativePrice() {
        String json = """
            {
                "name": "Test Product",
                "price": -10.0
            }
        """;

        webTestClient.post()
                .uri("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateProduct() {
        String json = """
                {
                    "price": 4.50,
                    "quantity": 3
                }
                """;

        webTestClient.put()
                .uri("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Test Product")
                .jsonPath("$.price").isEqualTo(4.5)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.quantity").isEqualTo(3);
    }

    @Test
    @Order(3)
    void testUpdateProductWithPriceEqualToZero() {
        String json = """
                {
                    "price": 0.0,
                    "quantity": 3
                }
                """;

        webTestClient.put()
                .uri("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateProductWithNegativePrice() {
        String json = """
                {
                    "price": -4.50,
                    "quantity": 3
                }
                """;

        webTestClient.put()
                .uri("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateProductWithNegativeQuantity() {

        String json = """
                {
                    "price": 4.50,
                    "quantity": -3
                }
                """;

        webTestClient.put()
                .uri("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(100)
    void testUpdateProductWithQuantityEqualToZero() {
        String json = """
                {
                    "price": 4.50,
                    "quantity": 0
                }
                """;

        webTestClient.put()
                .uri("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(100)
    void testUpdateNonExistingProduct() {
        String json = """
                {
                    "price": 4.50,
                    "quantity": 3
                }
                """;

        webTestClient.put()
                .uri("/product/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(2)
    void testGetProduct() {
        webTestClient.get()
                .uri("/product/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Test Product")
                .jsonPath("$.price").isEqualTo(10.5)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.quantity").isEqualTo(0);
    }

    @Test
    @Order(4)
    void getAllProducts() {
        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Test Product")
                .jsonPath("$[0].price").isEqualTo(4.5)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].quantity").isEqualTo(3);
    }
}