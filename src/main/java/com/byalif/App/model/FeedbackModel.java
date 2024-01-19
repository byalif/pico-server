package com.byalif.App.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackModel {
	private String email;
	private String comment;

	private boolean like;

	private boolean follow;
}
