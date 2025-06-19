package ru.job4j.persons.service;

import ru.job4j.persons.domain.Person;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PersonService {

	Optional<Person> save(Person person);

	Optional<Person> findById(int id);

	List<Person> findAll();

	boolean update(Person person);

	boolean delete(Person person);
}
