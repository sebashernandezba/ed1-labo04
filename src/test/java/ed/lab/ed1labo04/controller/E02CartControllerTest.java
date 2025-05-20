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

@AutoConfigureWebTestClient(timeout = "30s")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E02CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void createCart() {
        createProducts();

        String json = """
                {
                    "cartItems": [
                        {
                            "productId": 1,
                            "quantity": 5
                        },
                        {
                            "productId": 2,
                            "quantity": 10
                        }
                    ]
                }
                """;

        webTestClient.post()
                .uri("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.totalPrice").isEqualTo(75.0)
                .jsonPath("$.cartItems[0].name").isEqualTo("Test Product 1")
                .jsonPath("$.cartItems[0].price").isEqualTo(5.0)
                .jsonPath("$.cartItems[0].quantity").isEqualTo(5)
                .jsonPath("$.cartItems[1].name").isEqualTo("Test Product 2")
                .jsonPath("$.cartItems[1].price").isEqualTo(5.0)
                .jsonPath("$.cartItems[1].quantity").isEqualTo(10);

        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].quantity").isEqualTo(995)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].quantity").isEqualTo(990)
                .jsonPath("$[2].id").isEqualTo(3)
                .jsonPath("$[2].quantity").isEqualTo(1000);
    }

    @Test
    @Order(2)
    void getCart() {
        webTestClient.get()
                .uri("/cart/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.totalPrice").isEqualTo(75.0)
                .jsonPath("$.cartItems[0].name").isEqualTo("Test Product 1")
                .jsonPath("$.cartItems[0].price").isEqualTo(5.0)
                .jsonPath("$.cartItems[0].quantity").isEqualTo(5)
                .jsonPath("$.cartItems[1].name").isEqualTo("Test Product 2")
                .jsonPath("$.cartItems[1].price").isEqualTo(5.0)
                .jsonPath("$.cartItems[1].quantity").isEqualTo(10);
    }

    @Test
    @Order(3)
    void getNonExistingCart() {
        webTestClient.get()
                .uri("/cart/1999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void createCartWithNonExistingProduct() {
        String json = """
                {
                    "cartItems": [
                        {
                            "productId": 19912,
                            "quantity": 5
                        }
                    ]
                }
                """;

        webTestClient.post()
                .uri("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void createCartWithNotEnoughProduct() {
        String json = """
                {
                    "cartItems": [
                        {
                            "productId": 1,
                            "quantity": 50000
                        }
                    ]
                }
                """;

        webTestClient.post()
                .uri("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void createCartWithNegativeQuantity() {
        String json = """
                {
                    "cartItems": [
                        {
                            "productId": 1,
                            "quantity": -1
                        }
                    ]
                }
                """;

        webTestClient.post()
                .uri("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest();
    }

    void createProducts() {
        String createJson = """
            {
                "name": "Test Product %s",
                "price": 5.00
            }
        """;

        String updateJson = """
                {
                    "price": 5.00,
                    "quantity": 1000
                }
                """;


        for (int i = 1; i <= 3; i++) {
            webTestClient.post()
                    .uri("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(String.format(createJson, i))
                    .exchange()
                    .expectStatus().isCreated();

            webTestClient.put()
                    .uri("/product/" + i)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateJson)
                    .exchange()
                    .expectStatus().isOk();

        }
    }
}