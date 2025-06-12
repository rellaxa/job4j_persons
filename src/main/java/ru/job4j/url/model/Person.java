package ru.job4j.url.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Person {

	private String username;

	private String password;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Person person = (Person) o;
		return Objects.equals(username, person.username) &&
				Objects.equals(password, person.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, password);
	}
}