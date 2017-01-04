package farm.chaos.ppfax.model;

public enum UserStatus {

	REQUESTED("requested"),
	ACTIVE("active"),
	REJECTED("rejected"),
	INACTIVE("inactive"),
	BLOCKED("blocked");

	UserStatus(String s) {
		status = s;
	}
	private String status;

	public String getName() {
		return status;
	}
}
