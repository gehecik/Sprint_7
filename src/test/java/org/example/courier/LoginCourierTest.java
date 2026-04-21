package org.example.courier;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.BaseTest;
import org.example.steps.BaseSteps;
import org.example.data.Courier;
import org.example.steps.CourierSteps;
import org.junit.jupiter.api.*;

import java.net.HttpURLConnection;

public class LoginCourierTest extends BaseTest {
    private final CourierSteps courierTest = new CourierSteps();

    Courier courier;

    int courierId;

    @BeforeEach
    public void setUpLoginCourier() {
        courier = Courier.courierWithRandomLogin();
        courierTest.createCourier(courier);

        Response loginResponse = courierTest.loginCourier(courier);
        courierId = BaseSteps.verifyResponseId(loginResponse);
    }

    @Test
    @DisplayName("Successful login to an account")
    @Description("200: Successful login to an account")
    public void CheckLoginCourierSuccessfulTest() {
        Response response = courierTest.loginCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_OK);
    }

    public void CheckLoginCourierBadRequestTest(Object courier) {
        Response response = courierTest.loginCourierWithLog(courier);//.loginCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_BAD_REQUEST);
        BaseSteps.verifyResponse(response, "message","Недостаточно данных для входа");
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
    }

    public void CheckLoginNotFound(Object courier) {
        Response response = courierTest.loginCourier(courier);
        BaseSteps.checkStatusCode(response, HttpURLConnection.HTTP_NOT_FOUND);
        BaseSteps.verifyResponse(response, "message","Учетная запись не найдена");
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
