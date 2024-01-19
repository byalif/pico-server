package com.byalif.App.message;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedIn {
	private HttpStatus status;
	private String email;
	private boolean loggedin;
}
