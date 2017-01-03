package farm.chaos.ppfax.utils;

public class StringUtils {

	public static long atol(String s) {
		if (s == null) return 0;
		long result = 0;
		try {
			result = Long.parseLong(s);
		}
		catch (NumberFormatException e) {
		}

		return result;
	}

	public static int atoi(String s) {
		if (s == null) return 0;
		int result = 0;
		try {
			result = Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
		}

		return result;
	}

	public static boolean atob(String s) {
		return (s != null && (s.equals("1") || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes")));
	}

}
