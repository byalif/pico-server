package com.byalif.App.message;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class registered {
	private HttpStatus status;
	private boolean registered;
	private String message;
}
