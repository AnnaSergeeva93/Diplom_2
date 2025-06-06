package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.dto.OrderCreateRequest;
import ru.praktikum.dto.UserCreateAndEditRequest;
import ru.praktikum.dto.UserLoginRequest;
import ru.praktikum.steps.OrderSteps;
import ru.praktikum.steps.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class OrderCreateTest {

    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();

    private String email = userSteps.generateRandomEmail();
    private String password = userSteps.generateRandomPassword();
    private String name = userSteps.generateRandomName();
    public static List<String> ingredients = new ArrayList<>();
    private String accessToken;

    @Before
    public void setUp() {
        ingredients.clear();
        UserCreateAndEditRequest userCreateAndEditRequest = new UserCreateAndEditRequest(email, password, name);
        userSteps.userCreate(userCreateAndEditRequest);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Успешное создание заказа с авторизацией - возвращает 200 ОК")
    public void orderCreateWithLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.orderCreateWithLogin(userLoginRequest, orderCreateRequest)
                .statusCode(SC_OK)
                .body("success", is(true), "order.owner.email", equalTo(email.toLowerCase()));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Успешное создание заказа без авторизации - возвращает 200 ОК")
    public void orderCreateWithoutLogin() {
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.orderCreateWithoutLogin(orderCreateRequest)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Создание заказа без ингредиентов - возвращает 400 Bad Request")
    public void orderCreateWithoutIngredients() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.orderCreateWithLogin(userLoginRequest, orderCreateRequest)
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным ингредиентом")
    @Description("Создание заказа с неверным ингредиентом - возвращает 500 Internal Server Error")
    public void orderCreateWithErrorIngredient() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        ingredients.add("something");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.orderCreateWithLogin(userLoginRequest, orderCreateRequest)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
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
