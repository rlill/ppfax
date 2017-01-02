package farm.chaos.ppfax.utils;

@SuppressWarnings("serial")
public class CronValueOutOfRangeException extends Exception {

	public CronValueOutOfRangeException() {
		super();
	}

	public CronValueOutOfRangeException(String message) {
		super(message);
	}

	public CronValueOutOfRangeException(String message, Throwable e) {
		super(message, e);
	}

}
