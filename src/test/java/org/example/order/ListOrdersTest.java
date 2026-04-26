package org.example.order;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.steps.CourierSteps;
import org.example.data.Courier;
import org.example.steps.OrderSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.example.utils.RandomValue.randomNumber;

public class ListOrdersTest extends BaseTest {
    private final OrderSteps orderTest = new OrderSteps();
    private final CourierSteps courierTest = new CourierSteps();

    Courier courier;
    int courierId;

    @Test
    @DisplayName("Get list of orders")
    @Description("200: Response contains list of orders")
    void getOrdersListSuccessfulTest() {
        Map<String, Object> parameters = new HashMap<>();
        Response response = orderTest.getOrders(parameters);

        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        BaseSteps.checkIsNotEmpty(response, "orders");
        BaseSteps.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with existent id")
    @Description("200: Get list of orders with existent id")
    public void getOrdersListWithIdTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(loginResponse);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", courierId);

        Response response = orderTest.getOrders(parameters);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        BaseSteps.checkIsNotEmpty(response, "orders");
        BaseSteps.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with existent id and nearest station")
    @Description("200: Get list of orders with existent id and nearest station")
    public void getOrdersListWithIdAndStationsTest() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(loginResponse);
        String nearestStation = orderTest.getListWithStations();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", courierId);
        parameters.put("nearestStation", nearestStation);

        Response response = orderTest.getOrders(parameters);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        BaseSteps.checkIsNotEmpty(response, "orders");
        BaseSteps.checkIsList(response, "orders");
    }


    @Test
    @DisplayName("Get list of orders with limit")
    @Description("200: Get list of orders with limit")
    public void getOrdersListWithLimitTest() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("limit", orderTest.getDefaultLimit());
        parameters.put("page", orderTest.getDefaultPage());

        Response response = orderTest.getOrders(parameters);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        BaseSteps.checkIsNotEmpty(response, "orders");
        BaseSteps.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with limit and station")
    @Description("200: Get list of orders with limit and station Калужская")
    public void getOrdersListWithLimitAndStationTest() {
        String nearestStation = orderTest.getListWithStation();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("limit", orderTest.getDefaultLimit());
        parameters.put("page", orderTest.getDefaultPage());
        parameters.put("nearestStation", nearestStation);

        Response response = orderTest.getOrders(parameters);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
        BaseSteps.checkIsNotEmpty(response, "orders");
        BaseSteps.checkIsList(response, "orders");
    }

    @Test
    @DisplayName("Get list of orders with non-existent id")
    @Description("404: Get list of orders with non-existent id")
    public void getOrdersListNotFoundIdTest() {
        int randCourierId = randomNumber();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courierId", randCourierId);

        Response response = orderTest.getOrders(parameters);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        BaseSteps.verifyResponse(response, "message","Курьер с идентификатором " + randCourierId + " не найден");
    }


    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
    }

}
