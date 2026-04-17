package org.example.data;

import static org.example.utils.RandomValue.randomLogin;
import static org.example.utils.RandomValue.randomPassword;

public class Courier {

    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {
    }

    public static Courier courierWithRandomLogin() {
        return new Courier(randomLogin(),
                randomPassword(),
                "firstname");
    }

    public static Courier courierWithoutLogin() {
        return new Courier(null,
                randomPassword(),
                "firstname");
    }

    public static Courier courierWithoutPassword() {
        return new Courier(randomLogin(),
                null,
                "firstname");
    }

    public static Courier courierWrongPassword(Courier courier) {
        return new Courier(courier.getLogin(),
                "wrongPassword",
                courier.getFirstName());
    }

    public static Courier courierWrongLogin(Courier courier) {
        return new Courier(randomLogin(),
                courier.getPassword(),
                courier.getFirstName());
    }

    public static Courier currentCourierWithoutLogin(Courier courier) {
        return new Courier(null,
                courier.getPassword(),
                null);
    }

    public static Courier currentCourierWithoutPassword(Courier courier) {
        return new Courier(courier.getLogin(),
                null,
                null);
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
