package ru.praktikum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class OrderCreateRequest {
    private List<String> ingredients;
}
