package com.pokerrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Userstory voting inprogress")
public class UserStoryDeletionNotPermitted extends RuntimeException {

	public UserStoryDeletionNotPermitted(String exception) {
	}

}
