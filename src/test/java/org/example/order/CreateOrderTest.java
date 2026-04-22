package org.example.order;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.data.Order;
import org.example.steps.OrderSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.net.HttpURLConnection;
import java.util.stream.Stream;

public class CreateOrderTest extends BaseTest {
    private final OrderSteps orderTest = new OrderSteps();

    int track;

    @ParameterizedTest
    @NullSource
    @MethodSource("colorData")
    @DisplayName("Successful order creation")
    @Description("201: Successful order creation")
    void createOrderSuccessfulTest(String[] color) {
        Order order = Order.getOrder(color);

        Response response = orderTest.createOrder(order);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_CREATED);
        track = orderTest.getTrack(response);
        BaseSteps.verifyResponse(response, "track", track);
    }

    private static Stream<Arguments> colorData() {
        return Stream.of(
                Arguments.of((Object) new String[]{"BLACK"}),
                Arguments.of((Object) new String[]{"GREY"}),
                Arguments.of((Object) new String[]{"BLACK", "GREY"})
        );
    }

    @AfterEach
    public void tearDown() {
        if (track != 0) {
            orderTest.cancelOrder(track);
        }
    }
}
