package org.ghrepos;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ExampleTest {
    @Test
    void testHappyEndpoint() {
        given()
                .when().get("/users/happy")
                .then()
                .statusCode(200)
                .body(is("")); // All repos are forks. So they are not returned. But the user exists so the status is 200.
    }

}