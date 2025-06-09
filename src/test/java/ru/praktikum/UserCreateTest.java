package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.praktikum.dto.UserCreateAndEditRequest;
import ru.praktikum.dto.UserLoginRequest;
import ru.praktikum.steps.UserSteps;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

public class UserCreateTest {

    private UserSteps userSteps = new UserSteps();
    private String accessToken;


    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Успешное создание пользователя с уникальными данными - возвращает 200 ОК")
    public void createUser() {
        String email = userSteps.generateRandomEmail();
        String password = userSteps.generateRandomPassword();
        String name = userSteps.generateRandomName();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_OK)
                .body("success", is(true));

        accessToken = userSteps.userLoginAndGetToken(userLoginRequest);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Создание пользователя, который уже зарегистрирован - возвращает 403 Forbidden")
    public void createAlreadyExistedUser() {
        String email = userSteps.generateRandomEmail();
        String password = userSteps.generateRandomPassword();
        String name = userSteps.generateRandomName();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_OK)
                .body("success", is(true));

        accessToken = userSteps.userLoginAndGetToken(userLoginRequest);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Создание пользователя без поля email - возвращает 403 Forbidden")
    public void createUserWithNullEmail() {
        String password = userSteps.generateRandomPassword();
        String name = userSteps.generateRandomName();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(null, password, name);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым значением в поле email")
    @Description("Создание пользователя с пустым значением в поле email - возвращает 403 Forbidden")
    public void createUserWithEmptyEmail() {
        String password = userSteps.generateRandomPassword();
        String name = userSteps.generateRandomName();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest("", password, name);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Создание пользователя без поля password - возвращает 403 Forbidden")
    public void createUserWithNullPassword() {
        String email = userSteps.generateRandomEmail();
        String name = userSteps.generateRandomName();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, null, name);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым значением в поле password")
    @Description("Создание пользователя с пустым значением в поле password - возвращает 403 Forbidden")
    public void createUserWithEmptyPassword() {
        String email = userSteps.generateRandomEmail();
        String name = userSteps.generateRandomName();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, "", name);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Создание пользователя без поля name - возвращает 403 Forbidden")
    public void createUserWithNullName() {
        String password = userSteps.generateRandomPassword();
        String email = userSteps.generateRandomEmail();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, null);

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым значением в поле name")
    @Description("Создание пользователя с пустым значением в поле name - возвращает 403 Forbidden")
    public void createUserWithEmptyName() {
        String password = userSteps.generateRandomPassword();
        String email = userSteps.generateRandomEmail();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, "");

        userSteps
                .userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false), "message", is("Email, password and name are required fields"));
    }



    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken)
                    .statusCode(SC_ACCEPTED);
        }
    }


}

