package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.data.Courier;
import org.junit.jupiter.api.*;

import java.net.HttpURLConnection;

import static org.example.utils.EnvConfig.*;

public class LoginCourierTest {
    private final CourierTest courierTest = new CourierTest();
    Courier courier;

    int courierId;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = courierTest.verifyResponseId(loginResponse);
    }

    @Test
    @DisplayName("Successful login to an account")
    @Description("200: Successful login to an account")
    public void CheckLoginCourierSuccessfulTest() {
        Response response = courierTest.loginCourier(courier);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    public void CheckLoginCourierBadRequestTest(Object courier) {
        Response response = courierTest.loginCourierWithLog(courier);//.loginCourier(courier);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        courierTest.verifyResponse(response, "message","Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Logging an account without login")
    @Description("400: Logging an account without login")
    public void CheckLoginCourierBadRequestWithoutLoginTest() {
        Courier courierWithoutLogin = Courier.currentCourierWithoutLogin(courier);

        CheckLoginCourierBadRequestTest(courierWithoutLogin);
    }

    @Test
    @DisplayName("Logging an account without password")
    @Description("400: Logging an account without password")
    public void CheckLoginCourierBadRequestWithoutPasswordTest() {
        Courier courierWithoutPassword = Courier.currentCourierWithoutPassword(courier);

        CheckLoginCourierBadRequestTest(courierWithoutPassword);
        //Expected status code <400> but was <504>.
        //400 Bad Request "Недостаточно данных для входа"
        //504 Gateway Timeout
    }

    public void CheckLoginNotFound(Object courier) {
        Response response = courierTest.loginCourier(courier);
        courierTest.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        courierTest.verifyResponse(response, "message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Logging an account with non-existent login/password pair")
    @Description("404: Logging an account with non-existent login/password pair")
    public void CheckLoginNotFoundWithNonExistLoginPasswordPair() {
        Courier courier = Courier.courierWithRandomLogin();

        CheckLoginNotFound(courier);
    }

    @Test
    @DisplayName("Logging an account with wrong login")
    @Description("404: Logging an account with wrong login")
    public void CheckLoginNotFoundWithWrongLogin() {
        Courier courierWrongLogin = Courier.courierWrongLogin(courier);

        CheckLoginNotFound(courierWrongLogin);
    }

    @Test
    @DisplayName("Logging an account with wrong password")
    @Description("404: Logging an account with wrong password")
    public void CheckLoginNotFoundWithWrongPassword() {
        Courier courierWrongPassword = Courier.courierWrongPassword(courier);

        CheckLoginNotFound(courierWrongPassword);
    }

    @AfterEach
    public void tearDown() {
        if (courierId != 0) {
            courierTest.deleteById(courierId);
        }
    }

}
