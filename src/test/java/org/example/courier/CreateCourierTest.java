package org.example.courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.data.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.example.utils.EnvConfig.BASE_URL;
import static org.example.utils.EnvConfig.CREATE_COURIER;
import static org.example.utils.RandomLogin.randomLogin;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateCourierTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Successful account creation")
    @Description("201: Successful account creation")
    void CreateCourierCode201Test() {
        Courier courier = new Courier(randomLogin(),
                                           "1234",
                                           "firstname");

        Response response = sendPostRequestCourier(courier, CREATE_COURIER);
        checkStatusCode(response, 201);
        verifyResponse(response, "ok", true);
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

        CreateCourierCode400Test(courier, CREATE_COURIER);
    }

    @Test
    @DisplayName("Creating an account without a password")
    @Description("400: Creating an account without a password")
    void CreateCourierCode400WithoutPasswordTest() {
        String courier = "{ \"login\": \"loginname\"," +
                "\"firstName\": \"loginfirstname\" }";

        CreateCourierCode400Test(courier, CREATE_COURIER);
    }

    @Test
    @DisplayName("Creating an account with a duplicate login")
    @Description("409: Creating an account with a duplicate login")
    void CreateCourierCode409WithDuplicateLoginTest() {
        Courier courier = new Courier(randomLogin(),
                                           "1234",
                                           "firstname");

        sendPostRequestCourier(courier, CREATE_COURIER);

        Response response = sendPostRequestCourier(courier, CREATE_COURIER);
        checkStatusCode(response, 409);
        verifyResponse(response, "message","Этот логин уже используется");

        //Expected: Этот логин уже используется
        //  Actual: Этот логин уже используется. Попробуйте другой.
    }

    @Step("Send POST request to /api/v1/courier Create a new courier")
    public Response sendPostRequestCourier(Object courier, String endpoint) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(endpoint);
        return response;
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Verify response")
    public void verifyResponse(Response response, String key, Object expectedValue) {
        response.then().body(key, equalTo(expectedValue));
    }

    void CreateCourierCode400Test(Object courier, String endpoint) {
        Response response = sendPostRequestCourier(courier, endpoint);
        checkStatusCode(response, 400);
        verifyResponse(response, "message","Недостаточно данных для создания учетной записи");
    }
}
