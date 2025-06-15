package ru.job4j.persons.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.persons.domain.Person;
import ru.job4j.persons.repository.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonController.class);

	private final PersonRepository persons;

	private final PasswordEncoder encoder;

	@GetMapping("/")
	public List<Person> findAll() {
		log.info("Find all persons");
		return this.persons.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> findById(@PathVariable int id) {
		var person = this.persons.findById(id);
		log.info("Found person: {}", person);
		return new ResponseEntity<>(
				person.orElse(new Person()),
				person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
		);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Person> create(@RequestBody Person person) {
		person.setPassword(encoder.encode(person.getPassword()));
		log.info("Creating person: {}", person);
		return new ResponseEntity<>(
				this.persons.save(person),
				HttpStatus.CREATED
		);
	}

	@PutMapping("/")
	public ResponseEntity<Void> update(@RequestBody Person person) {
		person.setPassword(encoder.encode(person.getPassword()));
		log.info("Updating person: {}", person);
		this.persons.save(person);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		Person person = new Person();
		person.setId(id);
		log.info("Deleting person by id: {}", person);
		this.persons.delete(person);
		return ResponseEntity.ok().build();
	}

}