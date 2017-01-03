package farm.chaos.ppfax.model;

public enum UserRole {

	READER("Reader"),
	EDITOR("Editor"),
	MANAGER("Manager"),
	ADMIN("Administrator");

	private String role;
	UserRole(String r) {
		role = r;
	}

	public String getName() {
		return role;
	}

	public static UserRole getInstanceByValue(String s) {
		for (UserRole r : values())
			if (r.role.equals(s)) return r;
		return null;
	}
}
