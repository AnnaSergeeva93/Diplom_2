package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import ru.praktikum.dto.UserCreateAndEditRequest;
import ru.praktikum.dto.UserLoginRequest;
import ru.praktikum.dto.UserLoginResponse;

import static ru.praktikum.client.Client.spec;
import static ru.praktikum.constants.Url.*;

public class UserSteps {

    @Step("Генерация рандомного email пользователя")
    public String generateRandomEmail() {
        return RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    }

    @Step("Генерация рандомного пароля пользователя")
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Генерация рандомного имени пользователя")
    public String generateRandomName() {
        return RandomStringUtils.randomAlphabetic(10);
    }


    @Step("Создание нового пользователя")
    public ValidatableResponse userCreate(UserCreateAndEditRequest userCreateAndEditRequest) {
        return spec()
                .body(userCreateAndEditRequest)
                .post(USER_CREATE)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userLogin(UserLoginRequest userLoginRequest) {
        return spec()
                .body(userLoginRequest)
                .post(USER_LOGIN)
                .then();
    }

    @Step("Авторизация пользователя и получение accessToken")
    public String userLoginAndGetToken(UserLoginRequest userLoginRequest) {
        Response loginResponse = userLogin(userLoginRequest)
                .extract().response();
        return loginResponse.as(UserLoginResponse.class).getAccessToken();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse userEditWithLogin(UserLoginRequest userLoginRequest, UserCreateAndEditRequest userCreateAndEditRequest) {
        Response response = userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return spec()
                .header("Authorization", accessToken)
                .body(userCreateAndEditRequest)
                .patch(USER)
                .then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse userEditWithoutLogin(UserCreateAndEditRequest userCreateAndEditRequest) {
        return spec()
                .body(userCreateAndEditRequest)
                .patch(USER)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse userDelete(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .delete(USER)
                .then();
    }

}
