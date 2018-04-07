package wombatukun.tests.test6.exception;

public class ParserException extends RuntimeException {

	public ParserException(String message) {
		super(message);
	}

	public ParserException(String message, Throwable exception) {
		super(message, exception);
	}
}
