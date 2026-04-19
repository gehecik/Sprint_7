package org.example.order;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.data.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.example.utils.EnvConfig.BASE_URL;
import static org.example.utils.RandomValue.randomNumber;

public class GetOrderTest {
    private final OrderTest orderTest = new OrderTest();

    int track;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Response success")
    @Description("200: Response success")
    void getOrderSuccessfulTest() {
        Order order = Order.getOrderWithColor();

        Response response = orderTest.createOrder(order);

        track = orderTest.getTrack(response);
        Response getOrderResponse = orderTest.getOrderByTrack(track);
        orderTest.checkStatusCode(getOrderResponse, HttpURLConnection.HTTP_OK);
        orderTest.verifyResponseOrder(getOrderResponse, "order", order);
    }

    @Test
    @DisplayName("Response without track")
    @Description("400: Response without track")
    void getOrderBadRequestTest() {
        Response getOrderResponse = orderTest.getOrderWithoutTrack();
        orderTest.checkStatusCode(getOrderResponse, HttpURLConnection.HTTP_BAD_REQUEST);
        orderTest.verifyResponse(getOrderResponse, "message", "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Response with non-exist track")
    @Description("404: Response with non-exist track")
    void getOrderNotFoundTest() {
        int wrongTrack = randomNumber();

        Response getOrderResponse = orderTest.getOrderByTrack(wrongTrack);
        orderTest.checkStatusCode(getOrderResponse, HttpURLConnection.HTTP_NOT_FOUND);
        orderTest.verifyResponse(getOrderResponse, "message", "Заказ не найден");
    }

    @AfterEach
    public void tearDown() {
        if (track != 0) {
            orderTest.cancelOrder(track);
        }
    }
}
