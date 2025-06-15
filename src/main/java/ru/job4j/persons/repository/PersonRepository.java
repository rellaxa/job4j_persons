package ru.job4j.persons.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.persons.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Integer> {

	@Override
	List<Person> findAll();

	Optional<Person> findPersonByLogin(String login);
}
