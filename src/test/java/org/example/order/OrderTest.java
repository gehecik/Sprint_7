package org.example.order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.BaseTest;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderTest extends BaseTest {

    @Step("Create order")
    public Response createOrder(Object element) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(element)
                .post(BASE_PATH + "/orders");
        return response;
    }

    @Step("Cancel order")
    public void cancelOrder(int track) {
        given()
                .body(Map.of("track", track))
                .put(BASE_PATH + "/orders/cancel" );
    }

    @Step("Get orders list")
    public Response getOrders(Map<String, Object> parameters) {
        return given()
                .queryParams(parameters)
                .get(BASE_PATH + "/orders");
    }

//    @Step("Parameter for the request")
//    public void parameterForOrders(String name, Object value) {
//        given()
//
//    }
}
