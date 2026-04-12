package org.example;

import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.example.EnvConfig.BASE_URL;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateCourierTest {

    private static final String CREATE_COURIER_PATH = "/api/v1/courier";
    Random random = new Random();
    int randomNum = random.nextInt();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Successful account creation")
    @Description("201: Successful account creation")
    void CreateCourierCode201Test() {
        Courier courier = new Courier(String.format("user%d", randomNum),
                                           "1234",
                                           "firstname");

        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().statusCode(201)
                .body("ok", equalTo(true));
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"{ \"password\": \"1234\", " +
    //        "\"firstName\": \"loginfirstname\" }",
    //        "{ \"login\": \"loginname\"," +
    //        "\"firstName\": \"loginfirstname\" }"})
    @Test
    @DisplayName("Creating an account without a login")
    @Description("400: Creating an account without a login")
    void CreateCourierCode400WithoutLoginTest() {
        String body = "{ \"password\": \"12313\"," +
                "\"firstName\": \"loginfirstname\" }";

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(CREATE_COURIER_PATH)
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
                .post(CREATE_COURIER_PATH)
                .then().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Creating an account with a duplicate login")
    @Description("409: Creating an account with a duplicate login")
    void CreateCourierCode409WithDuplicateLoginTest() {
        Courier courier = new Courier(String.format("duplicateuser%d", randomNum),
                                           "1234",
                                           "firstname");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(CREATE_COURIER_PATH);

        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
        //Expected: Этот логин уже используется
        //  Actual: Этот логин уже используется. Попробуйте другой.
    }
}
