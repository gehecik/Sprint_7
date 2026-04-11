package org.example;

import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.example.EnvConfig.BASE_URL;

public class CreateCourierTest {

    //Courier courier = new Courier("ninja", "1234");
    String body = "{ \"login\": \"ninja\", \"password\": \"1234\" }";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Test")
    @Description("200: Test")
    void test() {
        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
    }
}
