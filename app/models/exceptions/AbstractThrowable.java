package models.exceptions;

public abstract class AbstractThrowable extends Throwable {
	private String message;

	public AbstractThrowable(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return super.toString() + " " + message;
	}

	public String getMessage() {
		return message;
	}
}
