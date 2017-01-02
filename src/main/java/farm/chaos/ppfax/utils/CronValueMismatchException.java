package farm.chaos.ppfax.utils;

@SuppressWarnings("serial")
public class CronValueMismatchException extends Exception {

	public CronValueMismatchException() {
		super();
	}

	public CronValueMismatchException(String message) {
		super(message);
	}

	public CronValueMismatchException(String message, Throwable e) {
		super(message, e);
	}

}
