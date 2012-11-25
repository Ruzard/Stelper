package models.exceptions;

import models.enums.ExceptionType;

public class PrivateMessageException extends Throwable {
	private ExceptionType type;
	private String message;

	public PrivateMessageException(ExceptionType exceptionType, String errorMessage) {
		type = exceptionType;
		message = errorMessage;
	}

	public ExceptionType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
