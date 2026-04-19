package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.data.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.example.utils.EnvConfig.BASE_URL;
import static org.example.utils.RandomValue.randomNumber;

public class DeleteCourierTest {
    private final CourierTest courierTest = new CourierTest();
    Courier courier;
    int courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Successful delete courier")
    @Description("200: Successful delete courier")
    public void deleteCourierSuccessfulTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response responselogin = courierTest.loginCourier(courier);
        courierId = courierTest.verifyResponseId(responselogin);
        Response response = courierTest.deleteById(courierId);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Delete courier with non-exist id")
    @Description("404: Delete courier with non-exist id")
    public void deleteCourierNotFoundWithNonExistIdTest() {
        courierId = randomNumber();
        Response response = courierTest.deleteById(courierId);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        courierTest.verifyResponse(response, "message","Курьера с таким id нет");
    }

    @Test
    @DisplayName("Delete courier without id")
    @Description("400: Delete courier without id")
    public void deleteCourierBadRequestWithoutIdTest() {
        Response response = courierTest.deleteWithoutId();
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        courierTest.verifyResponse(response, "message","Недостаточно данных для удаления курьера");
    }

}
