package farm.chaos.ppfax.security;

public enum PpfaxUserRole {

	EDITOR("Editor"),
	MANAGER("Manager"),
	ADMIN("Administrator");

	private String role;
	PpfaxUserRole(String r) {
		role = r;
	}

	public String getName() {
		return role;
	}

	public static PpfaxUserRole getInstanceByValue(String s) {
		for (PpfaxUserRole r : values())
			if (r.role.equals(s)) return r;
		return null;
	}
}
