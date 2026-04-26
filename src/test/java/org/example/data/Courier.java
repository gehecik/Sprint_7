package org.example.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.utils.RandomValue.randomNameWithDigit;
import static org.example.utils.RandomValue.randomNumberAsString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;

    public static Courier courierWithRandomLogin() {
        return Courier.builder()
                .login(randomNameWithDigit())
                .password(randomNumberAsString())
                .firstName("firstname")
                .build();
    }

    public static Courier courierWithoutLogin() {
        return Courier.builder()
                .password(randomNumberAsString())
                .firstName("firstname")
                .build();
    }

    public static Courier courierWithoutPassword() {
        return Courier.builder()
                .login(randomNameWithDigit())
                .firstName("firstname")
                .build();
    }

    public static Courier courierWrongPassword(Courier courier) {
        return Courier.builder()
                .login(courier.getLogin())
                .password("wrongPassword")
                .firstName(courier.getFirstName())
                .build();
    }

    public static Courier courierWrongLogin(Courier courier) {
        return Courier.builder()
                .login(randomNameWithDigit())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();
    }

    public static Courier currentCourierWithoutLogin(Courier courier) {
        return Courier.builder()
                .password(courier.getPassword())
                .build();
    }

    public static Courier currentCourierWithoutPassword(Courier courier) {
        return Courier.builder()
                .login(courier.getLogin())
                .build();
    }

}
