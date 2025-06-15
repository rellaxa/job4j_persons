package ru.job4j.persons.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.persons.repository.PersonRepository;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private PersonRepository persons;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		var personOpt = persons.findPersonByLogin(login);
		if (personOpt.isEmpty()) {
			throw new UsernameNotFoundException(login);
		}
		var person = personOpt.get();
		return new User(person.getLogin(), person.getPassword(), emptyList());
	}
}
