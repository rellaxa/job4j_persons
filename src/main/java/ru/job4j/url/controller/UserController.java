package ru.job4j.url.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.url.store.UserStore;
import ru.job4j.url.model.Person;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private UserStore users;

	private BCryptPasswordEncoder encoder;

	@PostMapping("/sign-up")
	public void signUp(@RequestBody Person person) {
		person.setPassword(encoder.encode(person.getPassword()));
		users.save(person);
	}

	@GetMapping("/all")
	public List<Person> findAll() {
		return users.findAll();
	}
}
