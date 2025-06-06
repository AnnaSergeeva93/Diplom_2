package ru.praktikum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserLoginResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;
}
