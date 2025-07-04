package ru.job4j.persons.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

	private final ObjectMapper objectMapper;

	public GlobalExceptionHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@ExceptionHandler(value = {NullPointerException.class})
	public void handleException(Exception e, HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType("application/json");
		response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
			{
				put("message", "Some of fields empty");
				put("details", e.getMessage());
			}
		}));
		LOGGER.error(e.getMessage());
	}

}
