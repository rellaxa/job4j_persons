package ru.job4j.persons.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordDto {

	@NotNull(message = "Id must be non null")
	@Min(1)
	private Integer userId;

	@NotEmpty
	private String password;
}
