package ru.praktikum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserLoginRequest {
    private String email;
    private String password;
}
