package ru.praktikum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserCreateAndEditRequest {
    private String email;
    private String password;
    private String name;
}
