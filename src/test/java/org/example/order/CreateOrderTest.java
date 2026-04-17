package org.example.order;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.data.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.net.HttpURLConnection;
import java.util.stream.Stream;

import static org.example.utils.EnvConfig.*;

public class CreateOrderTest {
    private final OrderTest orderTest = new OrderTest();

    int track;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("colorData")
    @DisplayName("Successful order creation")
    @Description("201: Successful order creation")
    void CreateOrderSuccessfulTest(String[] color) {
        Order order = new Order("firstName",
                "lastName",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color);


        Response response = orderTest.createOrder(order);
        orderTest.checkStatusCode(response, HttpURLConnection.HTTP_CREATED);
        track = orderTest.getTrack(response);
        orderTest.verifyResponse(response, "track", track);
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
