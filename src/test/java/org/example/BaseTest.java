package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

import static org.example.utils.EnvConfig.BASE_URL;

public class BaseTest {
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
}
