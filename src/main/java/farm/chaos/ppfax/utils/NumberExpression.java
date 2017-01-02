package farm.chaos.ppfax.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberExpression {

	private String xpr;
	private boolean all;
	private List<Range> tl = new ArrayList<>();
	private static Pattern RPT = Pattern.compile("(\\d+)(([\\-/])(\\d+))?");

	public NumberExpression(String expression) throws ImproperCronValueException {
		xpr = expression.trim();
		all = xpr.equals("*");
		if (!all) {
			for (String r : xpr.split(",")) {
				tl.add(new Range(r));
			}
		}
	}

	public boolean matches(int n) {
		if (all) return true;
		for (Range r : tl) {
			if (r.covers(n)) return true;
		}
		return false;
	}

	public boolean matchesAll() {
		return all;
	}

	public boolean exceedsRange(int from, int to) {
		for (Range r : tl) {
			switch (r.mode) {
			case POINT:
				if (r.t1 < from || r.t1 > to) return true;
				break;
			case RANGE:
				if (r.t1 < from || r.t2 < r.t1 || r.t2 > to) return true;
				break;
			case INTERVAL:
				if (r.t1 < from || r.t1 + r.t2 > to) return true;
				break;
			}
		}
		return false;
	}

	public boolean fitsRange(int from, int to) throws CronValueOutOfRangeException {
		for (Range r : tl) {
			switch (r.mode) {
			case POINT:
				if (r.t1 < from || r.t1 > to) throw new CronValueOutOfRangeException(String.format("Value %d exceeds range [%d - %d]", r.t1, from, to));
				break;
			case RANGE:
				if (r.t1 < from) throw new CronValueOutOfRangeException(String.format("First value (%d) lower than %d", r.t1, from));
				if (r.t2 < r.t1) throw new CronValueOutOfRangeException(String.format("Second value (%d) lower than first (%d)", r.t2, r.t1));
				if (r.t2 > to) throw new CronValueOutOfRangeException(String.format("Second value (%d) higher than %d", r.t2, to));
				break;
			case INTERVAL:
				if (r.t1 < from) throw new CronValueOutOfRangeException(String.format("First value (%d) lower than %d", r.t1, from));
				if (r.t2 > to) throw new CronValueOutOfRangeException(String.format("Second value (%d) higher than %d", r.t2, to));
				if (r.t1 + r.t2 > to) throw new CronValueOutOfRangeException(String.format("Increment (%d) higher than %d", r.t1 + r.t2, to));
				break;
			}
		}
		return true;
	}

	private class Range {
		public Range(String s) throws ImproperCronValueException {
			Matcher m = RPT.matcher(s);
			if (!m.matches()) throw new ImproperCronValueException("Not a number or a range: " + s);
			t1 = Integer.parseInt(m.group(1));
			String d = m.group(3);
			if (d != null) {
				switch (d) {
				case "-": mode = Mode.RANGE; break;
				case "/": mode = Mode.INTERVAL; break;
				default: throw new IllegalStateException("Unexpected mode identifier '" + d + "'");
				}
				t2 = Integer.parseInt(m.group(4));
			}
			else {
				mode = Mode.POINT;
			}
		}
		private int t1;
		private int t2;
		private Mode mode;

		public boolean covers(int a) {
			switch (mode) {
			case POINT:
				return (a == t1);
			case RANGE:
				return (a >= t1 && a <= t2);
			case INTERVAL:
				for (int i = t1; i <= a; i += t2)
					if (i == a) return true;
				return false;
			}
			return false;
		}

		@Override
		public String toString() {
			switch (mode) {
			case RANGE:
				return "Range [from:" + t1 + " to:" + t2 + "]";
			case INTERVAL:
				return "Range [start:" + t1 + " every:" + t2 + "]";
			case POINT:
				return "Range [value:" + t1 + "]";
			}
			return "unknown Range";
		}
	}

	private enum Mode {
		POINT, RANGE, INTERVAL;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NumberExpression [xpr=");
		builder.append(xpr);
		builder.append(", all=");
		builder.append(all);
		builder.append(", tl=");
		builder.append(tl);
		builder.append("]");
		return builder.toString();
	}

}
