package org.example;

import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.example.EnvConfig.BASE_URL;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateCourierTest {

    Random random = new Random();
    int randomNum = random.nextInt();

    Courier courier = new Courier(String.format("user%d", randomNum), "1234", "firstname");

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Successful account creation")
    @Description("201: Successful account creation")
    void CreateCourierCode201Test() {
        String body = "{ \"login\": \"loginname\"," +
                " \"password\": \"1234\", " +
                "\"firstName\": \"loginfirstname\" }";
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Creating an account without a login")
    @Description("400: Creating an account without a login")
    void CreateCourierCode400WithoutLoginTest() {
        String body = "{ \"password\": \"1234\", " +
                "\"firstName\": \"loginfirstname\" }";
        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Creating an account without a password")
    @Description("400: Creating an account without a password")
    void CreateCourierCode400WithoutPasswordTest() {
        String body = "{ \"login\": \"loginname\"," +
                "\"firstName\": \"loginfirstname\" }";
        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Creating an account with a duplicate login")
    @Description("409: Creating an account with a duplicate login")
    void CreateCourierCode409WithDuplicateLoginTest() {
        String body = "{ \"login\": \"loginname1\"," +
                " \"password\": \"1234\", " +
                "\"firstName\": \"loginfirstname\" }";
        given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
        //Expected: Этот логин уже используется
        //  Actual: Этот логин уже используется. Попробуйте другой.
    }
}
