package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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

        Response response = sendPostRequestCourier(courier);
        checkStatusCode(response, 201);
        verifyErrorMessage(response, "ok",true);
    }

    void CreateCourierCode400Test(String courier) {
        Response response = sendPostRequestCourier(courier);
        checkStatusCode(response, 400);
        verifyErrorMessage(response, "message","Недостаточно данных для создания учетной записи");
    }
    //@ParameterizedTest
    //@ValueSource(strings = {
    //     "{ \"password\": \"1234\", \"firstName\": \"loginfirstname\" }",
    //     "{ \"login\": \"loginname\", \"firstName\": \"loginfirstname\" }"
    //     })
    @Test
    @DisplayName("Creating an account without a login")
    @Description("400: Creating an account without a login")
    void CreateCourierCode400WithoutLoginTest() {
        String courier = "{ \"password\": \"12313\"," +
                "\"firstName\": \"loginfirstname\" }";

        CreateCourierCode400Test(courier);
    }

    @Test
    @DisplayName("Creating an account without a password")
    @Description("400: Creating an account without a password")
    void CreateCourierCode400WithoutPasswordTest() {
        String courier = "{ \"login\": \"loginname\"," +
                "\"firstName\": \"loginfirstname\" }";

        CreateCourierCode400Test(courier);
    }

    @Test
    @DisplayName("Creating an account with a duplicate login")
    @Description("409: Creating an account with a duplicate login")
    void CreateCourierCode409WithDuplicateLoginTest() {
        Courier courier = new Courier(String.format("duplicateuser%d", randomNum),
                                           "1234",
                                           "firstname");

        sendPostRequestCourier(courier);

        Response response = sendPostRequestCourier(courier);
        checkStatusCode(response, 409);
        verifyErrorMessage(response, "message","Этот логин уже используется");

        //Expected: Этот логин уже используется
        //  Actual: Этот логин уже используется. Попробуйте другой.
    }

    @Step("Send POST request to /api/v1/courier Create a new courier")
    public Response sendPostRequestCourier(Object courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
        return response;
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Verify response")
    public void verifyErrorMessage(Response response, String key, Object expectedValue) {
        response.then().body(key, equalTo(expectedValue));
    }

}
