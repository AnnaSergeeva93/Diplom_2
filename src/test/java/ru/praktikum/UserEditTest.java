package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.dto.UserCreateAndEditRequest;
import ru.praktikum.dto.UserLoginRequest;
import ru.praktikum.steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserEditTest {
    private UserSteps userSteps = new UserSteps();
    private String accessToken;

    private static String email = "anna.123@yandex.ru";
    private static String password = "hello12345";
    private static String name = "Anna";


    private static String newEmail = "masha.123@yandex.ru";
    private static String newPassword = "12345hello";
    private static String newName = "Masha";

    @Before
    public void setUp() {
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, name);
        userSteps.userCreate(userCreateAndEditRequest);
    }


    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Изменение данных поля email пользователя с авторизацией - возвращает 200 ОК")
    public void userEditEmailWithLogin() {
        UserCreateAndEditRequest userEditRequest = new UserCreateAndEditRequest(newEmail, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

        userSteps.userEditWithLogin(userLoginRequest, userEditRequest)
                .statusCode(SC_OK)
                .body("success", is(true), "user.email", equalTo(newEmail));

        email = newEmail;
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Изменение данных поля email пользователя без авторизации - возвращает 401 Unauthorized")
    public void userEditEmailWithoutLogin() {
        UserCreateAndEditRequest userEditRequest = new UserCreateAndEditRequest(newEmail, password, name);

        userSteps.userEditWithoutLogin(userEditRequest)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false), "message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение password пользователя с авторизацией")
    @Description("Изменение данных поля password пользователя с авторизацией - возвращает 200 ОК")
    public void userEditPasswordWithLogin() {
        UserCreateAndEditRequest userEditRequest = new UserCreateAndEditRequest(email, newPassword, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserLoginRequest userNewLoginRequest = new UserLoginRequest(email, newPassword);

        userSteps.userEditWithLogin(userLoginRequest, userEditRequest)
                .statusCode(SC_OK)
                .body("success", is(true));

        password = newPassword;

        userSteps.userLogin(userNewLoginRequest)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Изменение password пользователя без авторизации")
    @Description("Изменение данных поля password пользователя без авторизации - возвращает 401 Unauthorized")
    public void userEditPasswordWithoutLogin() {
        UserCreateAndEditRequest userEditRequest = new UserCreateAndEditRequest(email, newPassword, name);

        userSteps.userEditWithoutLogin(userEditRequest)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false), "message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение name пользователя с авторизацией")
    @Description("Изменение данных поля name пользователя с авторизацией - возвращает 200 ОК")
    public void userEditNameWithLogin() {
        UserCreateAndEditRequest userEditRequest = new UserCreateAndEditRequest(email, password, newName);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

        userSteps.userEditWithLogin(userLoginRequest, userEditRequest)
                .statusCode(SC_OK)
                .body("success", is(true), "user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Изменение name пользователя без авторизации")
    @Description("Изменение данных поля name пользователя без авторизации - возвращает 401 Unauthorized")
    public void userEditNameWithoutLogin() {
        UserCreateAndEditRequest userEditRequest = new UserCreateAndEditRequest(email, password, newName);

        userSteps.userEditWithoutLogin(userEditRequest)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false), "message", is("You should be authorised"));
    }


    @After
    public void tearDown() {
        if (accessToken == null) {
            UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
            accessToken = userSteps.userLoginAndGetToken(userLoginRequest);
        }
        if (accessToken != null) {
            userSteps.userDelete(accessToken)
                    .statusCode(SC_ACCEPTED);
        }
    }
}
