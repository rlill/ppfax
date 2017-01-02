package farm.chaos.ppfax.model;

public enum CronjobStatus {
	ENABLED("1"), DISABLED("0"), TEST("t"), XSLT("x");

	CronjobStatus(String c) {
		code = c;
	}

	private String code;
	public String getCode() {
		return code;
	}

	public static CronjobStatus getInstanceByCode(String c) {
		for (CronjobStatus s : values())
			if (s.code.equals(c)) return s;
		return null;
	}
}
