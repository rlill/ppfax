package farm.chaos.ppfax.utils;

import javax.servlet.ServletException;

public class InsufficientPermissionException extends ServletException {
	private static final long serialVersionUID = 2791405052134956072L;

	public InsufficientPermissionException() {
		super();
	}

	public InsufficientPermissionException(String msg) {
		super(msg);
	}

	public InsufficientPermissionException(String msg, Throwable reason) {
		super(msg, reason);
	}
}
