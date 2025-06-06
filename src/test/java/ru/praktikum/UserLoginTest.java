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
import static org.hamcrest.Matchers.is;

public class UserLoginTest {

    private UserSteps userSteps = new UserSteps();
    private String accessToken;
    private static String email = "anna.1234@yandex.ru";
    private static String password = "hello12345";
    private static String name = "Anna";

    @Before
    public void setUp() {
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, name);
        userSteps.userCreate(userCreateAndEditRequest);
    }


    @Test
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Авторизация под существующим пользователем - возвращает 200 ОК")
    public void userLogin () {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

        userSteps
                .userLogin(userLoginRequest)
                .statusCode(SC_OK)
                .body("success", is(true));

    }

    @Test
    @DisplayName("Авторизация c неверным значением в поле email")
    @Description("Авторизация c неверным значением в поле email - возвращает 401 Unauthorized")
    public void userLoginWithErrorEmail() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("something", password);

        userSteps
                .userLogin(userLoginRequest)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false),"message", is("email or password are incorrect"));

    }

    @Test
    @DisplayName("Авторизация c неверным значением в поле password")
    @Description("Авторизация c неверным значением в поле password - возвращает 401 Unauthorized")
    public void userLoginWithErrorPassword() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, "ohhellno");

        userSteps
                .userLogin(userLoginRequest)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false),"message", is("email or password are incorrect"));

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
