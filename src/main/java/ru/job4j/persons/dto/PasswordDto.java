package ru.job4j.persons.dto;

import lombok.Data;

@Data
public class PasswordDto {

	private Integer userId;

	private String password;
}
