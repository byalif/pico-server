package com.byalif.App.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Upload {
	private String title;
	private String description;
	private String image;
	private String email;
}
