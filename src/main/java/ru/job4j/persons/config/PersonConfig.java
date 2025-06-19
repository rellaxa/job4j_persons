package ru.job4j.persons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.job4j.persons.filter.JWTAuthenticationFilter;
import ru.job4j.persons.filter.JWTAuthorizationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static ru.job4j.persons.filter.JWTAuthenticationFilter.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class PersonConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   AuthenticationManager auth) throws Exception {
		JWTAuthenticationFilter authentFilter = new JWTAuthenticationFilter(auth);
		JWTAuthorizationFilter authorizFilter = new JWTAuthorizationFilter(auth);

		http
				.cors(withDefaults())
				.csrf(csrf -> csrf.disable())
				.addFilter(authentFilter)
				.addFilter(authorizFilter)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(authz -> authz
						.requestMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
						.requestMatchers("/error").permitAll()
						.anyRequest().authenticated()
				);

		return http.build();

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
