package ru.praktikum.client;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.praktikum.constants.Url;

import static io.restassured.RestAssured.given;

public class Client {
    public static RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Url.MAIN_URL)
                .log()
                .all();
    }
}
