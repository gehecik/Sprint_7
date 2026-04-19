package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.data.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.example.utils.EnvConfig.BASE_URL;

public class CreateCourierTest {
    private final CourierTest courierTest = new CourierTest();

    int courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Successful account creation")
    @Description("201: Successful account creation")
    void CreateCourierSuccessfulTest() {
        Courier courier = Courier.courierWithRandomLogin();

        Response response = courierTest.createCourier(courier);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_CREATED);
        courierTest.verifyResponse(response, "ok", true);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = courierTest.getId(loginResponse);
    }

    void CreateCourierBadRequestTest(Object courier) {
        Response response = courierTest.createCourier(courier);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        courierTest.verifyResponse(response, "message","Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Creating an account without a login")
    @Description("400: Creating an account without a login")
    void CreateCourierBadRequestWithoutLoginTest() {
        Courier courier = Courier.courierWithoutLogin();

        CreateCourierBadRequestTest(courier);
    }

    @Test
    @DisplayName("Creating an account without a password")
    @Description("400: Creating an account without a password")
    void CreateCourierBadRequestWithoutPasswordTest() {
        Courier courier = Courier.courierWithoutPassword();

        CreateCourierBadRequestTest(courier);
    }

    @Test
    @DisplayName("Creating an account with a duplicate login")
    @Description("409: Creating an account with a duplicate login")
    void CreateCourierConflictWithDuplicateLoginTest() {
        Courier courier = Courier.courierWithRandomLogin();

        courierTest.createCourier(courier);
        Response loginResponse = courierTest.loginCourier(courier);
        courierId = courierTest.getId(loginResponse);

        Response responseDuplicate = courierTest.createCourier(courier);
        courierTest.checkStatusCode(responseDuplicate, HttpURLConnection.HTTP_CONFLICT);
        courierTest.verifyResponse(responseDuplicate, "message","Этот логин уже используется");
    }

    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
    }

}
