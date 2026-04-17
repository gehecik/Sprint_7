package org.example.order;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.courier.CourierTest;
import org.example.data.Courier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.utils.EnvConfig.BASE_URL;
import static org.example.utils.RandomValue.randomId;

public class ListOrdersTest {
    private final OrderTest orderTest = new OrderTest();
    private final CourierTest courierTest = new CourierTest();
    Courier courier;
    int courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Get list of orders")
    @Description("200: Response contains list of orders")
    void getOrdersListSuccessfulTest() {
        Map<String, Object> parameters = new HashMap<>();
        Response response = orderTest.getOrders(parameters);

        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        orderTest.checkIsNotEmpty(response, "orders");
        orderTest.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with existent id")
    @Description("200: Get list of orders with existent id")
    public void getOrdersListWithIdTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = courierTest.verifyResponseId(loginResponse);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", courierId);

        Response response = orderTest.getOrders(parameters);
        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        orderTest.checkIsNotEmpty(response, "orders");
        orderTest.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with existent id and nearest station")
    @Description("200: Get list of orders with existent id and nearest station")
    public void getOrdersListWithIdAndStationsTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = courierTest.verifyResponseId(loginResponse);
        Gson gson = new Gson();
        List<String> stations = List.of("1", "2");
        String nearestStation = gson.toJson(stations);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", courierId);
        parameters.put("nearestStation", nearestStation);

        Response response = orderTest.getOrders(parameters);
        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        orderTest.checkIsNotEmpty(response, "orders");
        orderTest.checkIsList(response, "orders");
    }


    @Test
    @DisplayName("Get list of orders with limit")
    @Description("200: Get list of orders with limit")
    public void getOrdersListWithLimitTest() {
        int defaultLimit = 30;
        int defaultPage = 0;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("limit", defaultLimit);
        parameters.put("page", defaultPage);

        Response response = orderTest.getOrders(parameters);
        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        orderTest.checkIsNotEmpty(response, "orders");
        orderTest.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with limit and station")
    @Description("200: Get list of orders with limit and station Калужская")
    public void getOrdersListWithLimitAndStationTest() {
        int defaultLimit = 30;
        int defaultPage = 0;
        String nearestStation = new Gson().toJson(List.of("110"));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("limit", defaultLimit);
        parameters.put("page", defaultPage);
        parameters.put("nearestStation", nearestStation);

        Response response = orderTest.getOrders(parameters);
        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        orderTest.checkIsNotEmpty(response, "orders");
        orderTest.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with non-existent id")
    @Description("404: Get list of orders with non-existent id")
    public void getOrdersListNotFoundIdTest() {
        int randCourierId = randomId();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", randCourierId);

        Response response = orderTest.getOrders(parameters);
        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        orderTest.verifyResponse(response, "message","Курьер с идентификатором " + randCourierId + " не найден");
    }


    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
    }
}
