package ru.job4j.url.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import ru.job4j.url.filter.JWTAuthenticationFilter;
import ru.job4j.url.filter.JWTAuthorizationFilter;
import ru.job4j.url.service.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;
import static ru.job4j.url.filter.JWTAuthenticationFilter.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	public WebSecurity() {
		System.out.println("WebSecurity is created");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   AuthenticationManager authManager) throws Exception {

		JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(authManager);
		JWTAuthorizationFilter authorizationFilter = new JWTAuthorizationFilter(authManager);

		http
				.cors(withDefaults())
				.csrf(csrf -> csrf.disable())
				.addFilter(authFilter)
				.addFilter(authorizationFilter)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(authz -> authz
						.requestMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
						.anyRequest().authenticated()
				);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}