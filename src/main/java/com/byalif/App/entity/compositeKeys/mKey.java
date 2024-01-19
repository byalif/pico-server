package com.byalif.App.entity.compositeKeys;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class mKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private String email;
	private User user;

}
