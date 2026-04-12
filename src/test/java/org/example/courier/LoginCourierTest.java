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
import static org.example.utils.EnvConfig.*;
import static org.example.utils.RandomLogin.randomLogin;
import static org.hamcrest.core.IsEqual.equalTo;

public class LoginCourierTest {
    Courier courier = new Courier(randomLogin(),
            "1234",
            "firstname");

    Response createResponse;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        createResponse = sendPostRequestCourier(courier, CREATE_COURIER);
    }

    @Test
    @DisplayName("Successful login to an account")
    @Description("200: Successful login to an account")
    void CheckLoginCourierCode200Test() {
        Response response = sendPostRequestCourier(courier, LOGIN_COURIER);
        checkStatusCode(response, 200);

        int id = getId(response);

        verifyResponse(response, "id", id);
        deleteById(id);
    }

    @Test
    @DisplayName("Logging an account without a login")
    @Description("400: Logging an account without a login")
    void CheckLoginCourierCode400WithoutLoginTest() {
        String courierWithoutLogin = "{ \"password\": \"" +  courier.getPassword() + "\" }";

        CheckLoginCourierCode400Test(courierWithoutLogin, LOGIN_COURIER);
    }

    @Test
    @DisplayName("Logging an account without a password")
    @Description("400: Logging an account without a password")
    void CheckLoginCourierCode400WithoutPasswordTest() {
        String courierWithoutPassword = "{ \"login\": \"" +  courier.getLogin() + "\" }";

        CheckLoginCourierCode400Test(courierWithoutPassword, LOGIN_COURIER);
        //Expected status code <400> but was <504>.
        //400 Bad Request "Недостаточно данных для входа"
        //504 Gateway Timeout
    }

    @Test
    @DisplayName("Logging an account with a non-existent login/password pair")
    @Description("404: Logging an account with a non-existent login/password pair")
    void CheckLoginCode404WithNonExistLoginPasswordPair() {
        Courier courier = new Courier(randomLogin(),
                "1234",
                "firstname");

        CheckLoginCode404(courier, LOGIN_COURIER);
    }

    @Test
    @DisplayName("Logging an account with wrong login")
    @Description("404: Logging an account with wrong login")
    void CheckLoginCode404WithWrongLogin() {
        Courier courierWrongLogin = new Courier(randomLogin(),
                courier.getPassword(),
                courier.getFirstName());

        CheckLoginCode404(courierWrongLogin, LOGIN_COURIER);
    }

    @Test
    @DisplayName("Logging an account with wrong password")
    @Description("404: Logging an account with wrong password")
    void CheckLoginCode404WithWrongPassword() {
        Courier courierWrongPassword = new Courier(courier.getLogin(),
                "wrongPassword",
                courier.getFirstName());

        CheckLoginCode404(courierWrongPassword, LOGIN_COURIER);
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Verify response")
    public void verifyResponse(Response response, String key, Object expectedValue) {
        response.then().body(key, equalTo(expectedValue));
    }

    public Response sendPostRequestCourier(Object courier, String endpoint) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(endpoint);
        return response;
    }

    public Response sendPostRequestCourierWithLog(Object courier, String endpoint) {
        Response response = given()
                .log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
        return response;
    }

    public int getId(Response response) {
        return response.then().extract().path("id");
    }

    public void deleteById(int id) {
        given().delete(CREATE_COURIER + "/" + id);
    }

    void CheckLoginCourierCode400Test(Object courier, String endpoint) {
        Response response = sendPostRequestCourier(courier, endpoint);
        checkStatusCode(response, 400);
        verifyResponse(response, "message","Недостаточно данных для входа");
    }

    void CheckLoginCode404(Object courier, String endpoint) {
        Response response = sendPostRequestCourier(courier, endpoint);
        checkStatusCode(response, 404);
        verifyResponse(response, "message","Учетная запись не найдена");
    }
}
