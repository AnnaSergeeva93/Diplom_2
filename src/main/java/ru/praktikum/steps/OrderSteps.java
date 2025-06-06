package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.dto.OrderCreateRequest;
import ru.praktikum.dto.UserLoginRequest;
import ru.praktikum.dto.UserLoginResponse;

import static ru.praktikum.client.Client.spec;
import static ru.praktikum.constants.Url.ORDERS;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse orderCreateWithLogin(UserLoginRequest userLoginRequest, OrderCreateRequest orderCreateRequest) {
        UserSteps userSteps = new UserSteps();
        Response response = userSteps.userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return spec()
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .post(ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse orderCreateWithoutLogin(OrderCreateRequest orderCreateRequest) {
        return spec()
                .body(orderCreateRequest)
                .post(ORDERS)
                .then();
    }

    @Step("Получение заказов пользователя с авторизацией")
    public ValidatableResponse orderGetListWithLogin(UserLoginRequest userLoginRequest) {
        UserSteps userSteps = new UserSteps();
        Response response = userSteps.userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return spec()
                .header("Authorization", accessToken)
                .get(ORDERS)
                .then();
    }

    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse orderGetListWithoutLogin() {
        return spec()
                .get(ORDERS)
                .then();
    }
}
