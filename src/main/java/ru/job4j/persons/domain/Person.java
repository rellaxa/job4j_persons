package ru.job4j.persons.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.persons.util.Operation;

@Entity
@Data
@Table(name = "persons")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@NotNull(message = "Id must be non null", groups = {
			Operation.OnUpdate.class, Operation.OnDelete.class
	})
	@Min(value = 1, groups = {
			Operation.OnUpdate.class, Operation.OnDelete.class
	})
	private Integer id;

	@NotEmpty(message = "Login must not be null nor empty", groups = {
			Operation.OnCreate.class, Operation.OnUpdate.class, Operation.OnDelete.class
	})
	private String login;

	@NotEmpty(message = "Password must not be null nor empty", groups = {
			Operation.OnCreate.class, Operation.OnUpdate.class, Operation.OnDelete.class
	})
	private String password;

}
