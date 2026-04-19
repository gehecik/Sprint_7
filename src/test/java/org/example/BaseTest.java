package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matcher;

import java.util.List;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

public class BaseTest {
    public static final String BASE_PATH = "/api/v1";

    @Step("Check status code")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Verify response")
    public void verifyResponse(Response response, String key, Object expectedValue) {
        response.then().body(key, equalTo(expectedValue));
    }

    @Step("Verify part of response")
    public void verifyPartOfResponse(Response response, String key, String expectedValue) {
        response.then().body(key, containsString(expectedValue));
    }

    @Step("Verify part of response")
    public void verifyHasItemResponse(Response response, String key, String expectedValue) {
        response.then().body(key, hasItem(expectedValue));
    }

    @Step("Verify response")
    public void checkOrder(Response response, String key, Matcher expectedValue) {
        response.then().body(key, expectedValue);
    }

    @Step("Check is not empty")
    public void checkIsNotEmpty(Response response, String key) {
        response.then().body(key, is(not(empty())));
    }

    @Step("Check is list")
    public void checkIsList(Response response, String key) {
        response.then().body(key, isA(List.class));
    }


    public int getValue(Response response, String name) {
        return response.then().extract().path(name);
    }

    public int getId(Response response) {
        return getValue(response,"id");
    }

    public int getTrack(Response response) {
        return getValue(response,"track");
    }

    public int verifyResponseId(Response response) {
        int id = getId(response);

        verifyResponse(response, "id", id);
        return id;
    }


}
