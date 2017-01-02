package farm.chaos.ppfax.utils;

@SuppressWarnings("serial")
public class ImproperCronValueException extends Exception {

	public ImproperCronValueException() {
		super();
	}

	public ImproperCronValueException(String message) {
		super(message);
	}

	public ImproperCronValueException(String message, Throwable e) {
		super(message, e);
	}

}
