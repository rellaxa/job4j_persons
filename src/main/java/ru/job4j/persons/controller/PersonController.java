package ru.job4j.persons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.persons.domain.Person;
import ru.job4j.persons.repository.PersonRepository;
import ru.job4j.persons.service.PersonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonController.class);

	private final PersonService personService;

	private final PasswordEncoder encoder;

	private final ObjectMapper objectMapper;

	@GetMapping("/")
	public List<Person> findAll() {
		log.info("Find all persons");
		return personService.findAll();
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Person> findById(@PathVariable int id) {
		return personService.findById(id)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(
								HttpStatus.NOT_FOUND, "Person is not found. Please, check id."
						)
				);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Person> create(@RequestBody Person person) {
		var login = person.getLogin();
		var password = person.getPassword();
		if (login == null || password == null) {
			throw new NullPointerException("Login and password are required.");
		}
		person.setPassword(encoder.encode(person.getPassword()));
		log.info("Creating person: {}", person);
		return personService.save(person)
				.map(saved -> ResponseEntity
						.status(HttpStatus.CREATED)
						.body(saved))
				.orElseThrow(() -> new DataIntegrityViolationException(
								String.format("Person with login: %s already exists.", login)
						)
				);
	}

	@PutMapping("/update")
	public ResponseEntity<String> update(@RequestBody Person person) {
		var id = person.getId();
		if (id == null || id < 1) {
			throw new IllegalArgumentException("Person id is required or should be a positive integer.");
		}
		var login = person.getLogin();
		var password = person.getPassword();
		if (login == null || password == null) {
			throw new NullPointerException("Login and password are required.");
		}
		person.setPassword(encoder.encode(person.getPassword()));
		log.info("Updating person: {}", person);
		var updated = personService.update(person);
		return updated ? ResponseEntity.ok().body("Updated is successful.") :
				ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(
						"Updated failed. Person with id: %s not found.", id)
				);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		if (id < 1) {
			throw new IllegalArgumentException("Person id must not less than one.");
		}
		Person person = new Person();
		person.setId(id);
		log.info("Deleting person by id: {}", person);
		var deleted = personService.delete(person);
		return deleted ? ResponseEntity.ok().body("Deleted is successful.") :
				ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(
						"Deleted failed. Person with id: %s not found.", id)
				);
	}

	@ExceptionHandler(value = {IllegalArgumentException.class, DataIntegrityViolationException.class})
	public void illegalHandler(Exception e, HttpServletRequest request,
							   HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType("application/json");
		response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
			{
				put("message", e.getMessage());
				put("type", e.getClass());
			}
		}));
		log.error(e.getLocalizedMessage());
	}

}