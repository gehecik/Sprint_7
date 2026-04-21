package org.example.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public Order(String firstName,
                 String lastName,
                 String address,
                 String metroStation,
                 String phone,
                 Integer rentTime,
                 String deliveryDate,
                 String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = null;
    }

    public static Order getOrder(String[] color) {
        return Order.builder()
                .firstName("firstName")
                .lastName("lastName")
                .address("Konoha, 142 apt.")
                .metroStation("4")
                .phone("+7 800 355 35 35")
                .rentTime(5)
                .deliveryDate("2020-06-06")
                .comment("Saske, come back to Konoha")
                .color(color)
                .build();
    }

    public static Order getOrderWithColor() {
        return Order.builder()
                .firstName("firstName")
                .lastName("lastName")
                .address("Konoha, 142 apt.")
                .metroStation("4")
                .phone("+7 800 355 35 35")
                .rentTime(5)
                .deliveryDate("2020-06-06")
                .comment("Saske, come back to Konoha")
                .color(new String[]{"BLACK"})
                .build();
    }

}
