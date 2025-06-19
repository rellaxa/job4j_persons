package ru.job4j.persons.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.job4j.persons.domain.Person;
import ru.job4j.persons.repository.PersonRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

	private PersonRepository persons;

	@Override
	public Optional<Person> save(Person person) {
		try {
			return Optional.of(persons.save(person));
		} catch (DataIntegrityViolationException e) {
			log.error("Attempt to save duplicate person with login={}", person.getLogin());
			return Optional.empty();
		}
	}

	@Override
	public Optional<Person> findById(int id) {
		return persons.findById(id);
	}

	@Override
	public List<Person> findAll() {
		return persons.findAll();
	}

	@Override
	public boolean update(Person person) {
		try {
			persons.save(person);
			return true;
		} catch (Exception e) {
			log.error("Person with id={} not found, cannot update.", person.getId());
			return false;
		}
	}

	@Override
	public boolean delete(Person person) {
		if (persons.existsById(person.getId())) {
			persons.delete(person);
			return true;
		}
		log.error("Person with id={} not found, cannot delete.", person.getId());
		return false;
	}
}
