package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static guru.springframework.spring6reactive.app.ApplicationConstants.BEER_PATH;
import static guru.springframework.spring6reactive.app.ApplicationConstants.BEER_PATH_ID;
import static guru.springframework.spring6reactive.repositories.BeerRepositoryTest.getTestBeer;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
public class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListBeers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(2)
    void testGetById() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .expectBody(BeerDTO.class);
    }

    @Test
    @Order(3)
    void testCreateBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    @Order(4)
    void testUpdateBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    void testPatchBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(6)
    void testDeleteBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BEER_PATH_ID, 1)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(7)
    void testCreateBeerBadData() {
        Beer testBeer = getTestBeer();
        testBeer.setBeerName("");
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post()
                .uri(BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(8)
    void testUpdateBeerBadRequest() {
        Beer testBeer = getTestBeer();
        testBeer.setBeerStyle("");
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BEER_PATH_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(9)
    void testGetBeerByIdNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get()
                .uri(BEER_PATH_ID, 999)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(10)
    void testUpdateBeerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put()
                .uri(BEER_PATH_ID, 999)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(11)
    void testPatchBeerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BEER_PATH_ID, 999)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(12)
    void testDeleteBeerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete()
                .uri(BEER_PATH_ID, 999)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}