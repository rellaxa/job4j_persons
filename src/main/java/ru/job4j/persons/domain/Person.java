package ru.job4j.persons.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "persons")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	private String login;

	private String password;

}
