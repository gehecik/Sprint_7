package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.BaseTest;

import static io.restassured.RestAssured.given;

public class CourierTest extends BaseTest {

    @Step("Create courier")
    public Response createCourier(Object courier) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(BASE_PATH + "/courier");
        return response;
    }

    @Step("Create courier with log")
    public Response createCourierWithLog(Object courier) {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(BASE_PATH + "/courier")
                .then()
                .log().all()
                .extract()
                .response();
        return response;
    }

    @Step("Login courier")
    public Response loginCourier(Object courier) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(BASE_PATH + "/courier/login");
        return response;
    }

    @Step("Login courier with log")
    public Response loginCourierWithLog(Object courier) {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(BASE_PATH + "/courier/login")
                .then()
                .log().all()
                .extract()
                .response();
        return response;
    }

    @Step("Delete courier")
    public Response deleteById(Object id) {
        return given()
                .delete(BASE_PATH + "/courier/{id}", id)
                .then()
                .extract()
                .response();
    }

    @Step("Delete courier")
    public Response deleteWithoutId() {
        return deleteById("null");
    }

}
