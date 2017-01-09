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

	public static long getIdFromUri(String requestURI) {
		if (requestURI == null) return 0;
		int p = requestURI.lastIndexOf('/');
		if (p < 1 || p >= requestURI.length() - 1) return 0;
		return atol(requestURI.substring(p + 1));
	}

	// --> /topic/topic/title-of-the-article-1234.html
	public static long getArticleIdFromUri(String requestURI) {
		if (requestURI == null) return 0;
		int p1 = requestURI.lastIndexOf('-');
		int p2 = requestURI.lastIndexOf('.');
		if (p1 < 1 || p1 >= p2 || p2 >= requestURI.length() - 1) return 0;
		return atol(requestURI.substring(p1 + 1, p2));
	}

}
