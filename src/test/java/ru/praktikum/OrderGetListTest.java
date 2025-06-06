package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.dto.UserCreateAndEditRequest;
import ru.praktikum.dto.UserLoginRequest;
import ru.praktikum.steps.OrderSteps;
import ru.praktikum.steps.UserSteps;


import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class OrderGetListTest {
    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();

    private String email = userSteps.generateRandomEmail();
    private String password = userSteps.generateRandomPassword();
    private String name = userSteps.generateRandomName();
    private String accessToken;

    @Before
    public void setUp() {
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, name);
        userSteps.userCreate(userCreateAndEditRequest);
    }

    @Test
    @DisplayName("Получение списка заказов пользователя с авторизацией")
    @Description("Успешное получение списка заказов пользователя с авторизацией - возвращает 200 ОК")
    public void orderGetListWithLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);

        orderSteps.orderGetListWithLogin(userLoginRequest)
                .statusCode(SC_OK)
                .body("success", is(true),"orders",instanceOf(List.class));
    }

    @Test
    @DisplayName("Получение списка заказов пользователя без авторизации")
    @Description("Получение списка заказов пользователя без авторизации - возвращает 401 Unauthorized")
    public void orderGetListWithoutLogin() {

        orderSteps.orderGetListWithoutLogin()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false),"message", is("You should be authorised"));
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
