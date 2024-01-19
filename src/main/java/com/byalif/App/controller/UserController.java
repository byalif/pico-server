package com.byalif.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.App.message.LoggedIn;
import com.byalif.App.message.Message;
import com.byalif.App.message.errorMessage;
import com.byalif.App.message.registered;
import com.byalif.App.model.Login;
import com.byalif.App.model.Upload;
import com.byalif.App.model.registerModel;
import com.byalif.App.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	@CrossOrigin
	public ResponseEntity<?> registerUser(@RequestBody registerModel user) {
		String status = userService.registerUser(user);
		if (status.equalsIgnoreCase("valid")) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new registered(HttpStatus.ACCEPTED, true, status.toUpperCase()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new registered(HttpStatus.BAD_REQUEST, false, status.toUpperCase()));
	}

	@PostMapping("/login")
	@CrossOrigin
	public ResponseEntity<?> loginUser(@RequestBody Login user) {
		String status = userService.findByEmail(user);
		if (status.equalsIgnoreCase("LOGGED_IN")) {
			return ResponseEntity.status(HttpStatus.OK).body(new LoggedIn(HttpStatus.OK, user.getEmail(), true));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new errorMessage(HttpStatus.BAD_REQUEST, status.toUpperCase(), false));
	}

	@PostMapping("/upload")
	@CrossOrigin
	public ResponseEntity<?> uploadImage(@RequestBody Upload upload) {
		String status = userService.postPicture(upload);
		if (status.equalsIgnoreCase("UPLOADED")) {
			return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.ACCEPTED, status));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new errorMessage(HttpStatus.BAD_REQUEST, status.toUpperCase(), false));
	}

}
