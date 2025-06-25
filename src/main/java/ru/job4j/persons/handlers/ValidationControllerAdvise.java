package ru.job4j.persons.handlers;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ValidationControllerAdvise {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handle(MethodArgumentNotValidException e) {
		var fieldErrors = e.getFieldErrors();
		log.error(fieldErrors.toString());
		return ResponseEntity.badRequest().body(
				fieldErrors.stream()
						.map(f -> Map.of(
								f.getField(),
								String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
						))
						.collect(Collectors.toList())
		);
	}

	@ExceptionHandler(MethodValidationException.class)
	public ResponseEntity<Map<String, String>> handle(MethodValidationException ex) {
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getParameterValidationResults().forEach(result -> {
			String name = result.getMethodParameter().getParameterName();
			String msg = result.getResolvableErrors().stream()
					.map(MessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.joining("; "));
			errors.put(name, msg);
		});
		log.error(errors.toString());
		return ResponseEntity.badRequest().body(errors);
	}

}