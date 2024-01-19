package com.byalif.App.message;

import org.springframework.http.HttpStatus;

import com.byalif.App.entity.Likes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
	Likes like;
	HttpStatus status;
}
