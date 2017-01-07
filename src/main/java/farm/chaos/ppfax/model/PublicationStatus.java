package farm.chaos.ppfax.model;

public enum PublicationStatus {

	OFFLINE("offline"), ONLINE("online"), DELETED("deleted");

	private String name;
	PublicationStatus(String n) {
		name = n;
	}

	public String getName() {
		return name;
	}
}
