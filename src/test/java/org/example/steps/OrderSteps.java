package org.example.steps;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.data.Order;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.example.steps.BaseSteps.BASE_PATH;

public class OrderSteps {

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


    public String getListWithParams(List<String> params) {
        Gson gson = new Gson();
        List<String> stations = params;
        return gson.toJson(stations);
    }

    @Step("Get orders list")
    public String getListWithStations() {
        return getListWithParams(List.of("1", "2"));
    }

    @Step("Get orders list")
    public String getListWithStation() {
        return getListWithParams(List.of("110"));
    }

    public int getDefaultLimit() {
        return 30;
    }

    public int getDefaultPage() {
        return 0;
    }

    @Step("Put order with params")
    public Response putOrder(Object id, Map<String, Object> parameters) {
        return given()
                .queryParams(parameters)
                .put(BASE_PATH + "/orders/accept/{id}", id);
    }

    @Step("Put order without id and with params")
    public Response putOrderWithoutId(Map<String, Object> parameters) {
        return given()
                .queryParams(parameters)
                .put(BASE_PATH + "/orders/accept/");
    }

    @Step("Get order by track")
    public Response getOrderByTrack(int track) {
        return given()
                .queryParam("t", track)
                .get(BASE_PATH + "/orders/track")
                .then()
                .extract()
                .response();
    }

    @Step("Get order without track")
    public Response getOrderWithoutTrack() {
        return given()
                .get(BASE_PATH + "/orders/track")
                .then()
                .extract()
                .response();
    }

    public int getOrderId(Response response) {
        return BaseSteps.getValue(response,"order.id");
    }


    public void verifyResponseOrder(Response response, String key, Order expectedValue) {
        BaseSteps.verifyResponse(response, key + ".firstName", expectedValue.getFirstName());
        BaseSteps.verifyResponse(response, key + ".lastName", expectedValue.getLastName());
        BaseSteps.verifyResponse(response, key + ".address", expectedValue.getAddress());
        BaseSteps.verifyResponse(response, key + ".metroStation", expectedValue.getMetroStation());
        BaseSteps.verifyResponse(response, key + ".phone", expectedValue.getPhone());
        BaseSteps.verifyResponse(response, key + ".rentTime", expectedValue.getRentTime());
        BaseSteps.verifyPartOfResponse(response, key + ".deliveryDate", expectedValue.getDeliveryDate());
        BaseSteps.verifyResponse(response, key + ".comment", expectedValue.getComment());
        BaseSteps.verifyHasItemResponse(response, key + ".color", expectedValue.getColor()[0]);
    }

    public int getTrack(Response response) {
        return BaseSteps.getValue(response,"track");
    }

}