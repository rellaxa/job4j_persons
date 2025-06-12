package ru.job4j.url.store;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.url.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserStore {

	private final ConcurrentHashMap<String, Person> users = new ConcurrentHashMap<>();

	public void save(Person person) {
		users.put(person.getUsername(), person);
	}

	public Person findByUsername(String username) {
		return users.get(username);
	}

	public List<Person> findAll() {
		return new ArrayList<>(users.values());
	}
}
