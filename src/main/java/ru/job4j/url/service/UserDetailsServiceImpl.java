package ru.job4j.url.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ru.job4j.url.store.UserStore;
import ru.job4j.url.model.Person;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserStore users;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person user = users.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(user.getUsername(), user.getPassword(), emptyList());
	}
}
