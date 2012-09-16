package models.enums;

public enum ResponseStatus {
	RATING_ALREADY_CHANGED("Rating has already been changed by you"),
	RATING_NEUTRAL_EXCEPTION("Post cannot receive neutral rating"),
	ACCESS_VIOLATION("Banned users do not have rights"),
	ROLLBACK_OCCURED("Handling exception, object deletion is needed"),
	DATA_VALIDATION_EXCEPTION("You have some data: "),
	OK("SUCCESS");


	private String message;

	ResponseStatus(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
