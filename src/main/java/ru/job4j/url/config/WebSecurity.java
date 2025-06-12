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
import ru.job4j.url.JWTAuthenticationFilter;
import ru.job4j.url.JWTAuthorizationFilter;
import ru.job4j.url.service.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;
import static ru.job4j.url.JWTAuthenticationFilter.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	private UserDetailsServiceImpl userDetailsService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(UserDetailsServiceImpl userDetailsService,
					   BCryptPasswordEncoder bCryptPasswordEncoder) {
		System.out.println("WebSecurity is created");
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   AuthenticationManager authManager) throws Exception {
		http
				.cors(withDefaults())
				.csrf(csrf -> csrf.disable())
				.httpBasic(basic -> basic.disable())
				.formLogin(login -> login.disable())
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(authz -> authz
						.requestMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
						.anyRequest().authenticated()
				);

		JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(authManager);
		JWTAuthorizationFilter authorizationFilter = new JWTAuthorizationFilter(authManager);
		http.addFilter(authFilter);
		http.addFilter(authorizationFilter);

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