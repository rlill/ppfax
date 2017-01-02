package farm.chaos.ppfax.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import farm.chaos.ppfax.model.CronLogLevel;
import farm.chaos.ppfax.model.Cronjob;
import farm.chaos.ppfax.model.CronjobStatus;
import farm.chaos.ppfax.persistance.Datastore;

public class CronProcessor {

	private static final Logger LOG = Logger.getLogger(CronProcessor.class.getName());

	public String processCronJobs() {
		Date now = new Date();
		for (Cronjob j : Datastore.getAllCronjobs()) {
			if (j.getNextRun() == null) {
				Date next = null;
				try {
					next = calcNextRun(j, now);
				} catch (ImproperCronValueException e) {
					LOG.log(Level.SEVERE, "Invalid cron value expression: " + e.getMessage(), e);
				}
				j.setNextRun(next);
				Datastore.saveCronjob(j);
			}
			else if (j.getNextRun().before(now) && j.getStatus() == CronjobStatus.ENABLED) {

				// run job
				try {

				}
				catch (Exception e) {
					LOG.log(Level.SEVERE, e.getMessage(), e);
		            Datastore.saveLogEntry(CronLogLevel.ERROR, "Error running job " + j.getId() + ": " + j.getCommand() + ": " + e.getMessage());
				}

				Date next = null;
				try {
					next = calcNextRun(j, now);
				} catch (ImproperCronValueException e) {
					LOG.log(Level.SEVERE, "Invalid cron value expression: " + e.getMessage(), e);
				}
				j.setLastRun(new Date());
				j.setNextRun(next);
				Datastore.saveCronjob(j);
			}
			else if (j.getNextRun().before(now) && j.getStatus() == CronjobStatus.TEST) {

				Datastore.saveLogEntry(CronLogLevel.INFO, "TEST for cronjob " + j.getId() + ": " + j.getCommand());

				Date next = null;
				try {
					next = calcNextRun(j, now);
				} catch (ImproperCronValueException e) {
					LOG.log(Level.SEVERE, "Invalid cron value expression: " + e.getMessage(), e);
				}
				j.setLastRun(new Date());
				j.setNextRun(next);
				Datastore.saveCronjob(j);
			}
			else if (j.getNextRun().before(now) && j.getStatus() == CronjobStatus.XSLT) {

				Queue queue = QueueFactory.getQueue("xslttask");

				queue.add(TaskOptions.Builder.withUrl("/xslttask").param("id", j.getCommand()));
				Datastore.saveLogEntry(CronLogLevel.INFO, "XSLT task " + j.getCommand() + " enqueued");

				Date next = null;
				try {
					next = calcNextRun(j, now);
				} catch (ImproperCronValueException e) {
					LOG.log(Level.SEVERE, "Invalid cron value expression: " + e.getMessage(), e);
				}
				j.setLastRun(new Date());
				j.setNextRun(next);
				Datastore.saveCronjob(j);
			}


		}

		return "OK";
	}


	/**
	 * Verifies whether all fields of a Cronjob object are valid.
	 * @param cronjob
	 * @return null if everything is ok, a String describing the error otherwise.
	 */
	public static String verifyParameters(Cronjob cronjob) {

		if (!cronjob.getDayOfMonth().equals("*") && !cronjob.getDayOfWeek().equals("*"))
			return "Cronjob cannot have both, day-of-month and day-of-week expression other than \"*\"";

    	NumberExpression xpr;

    	String minute = cronjob.getMinute();
    	try {
    		xpr = new NumberExpression(minute);
    		xpr.fitsRange(0, 59);
    	} catch (ImproperCronValueException e) {
    		return "Invalid expression for 'minute': " + e.getMessage();
    	} catch (CronValueOutOfRangeException e) {
    		return "Invalid value for 'minute': " + e.getMessage();
		}

    	String hour = cronjob.getHour();
    	try {
    		xpr = new NumberExpression(hour);
    		xpr.fitsRange(0, 23);
    	} catch (ImproperCronValueException e) {
    		return "Invalid expression for 'hour': " + e.getMessage();
    	} catch (CronValueOutOfRangeException e) {
    		return "Invalid value for 'hour': " + e.getMessage();
    	}

    	String dayOfMonth = cronjob.getDayOfMonth();
    	try {
    		xpr = new NumberExpression(dayOfMonth);
    		xpr.fitsRange(1, 31);
    	} catch (ImproperCronValueException e) {
    		return "Invalid expression for 'day of month': " + e.getMessage();
    	} catch (CronValueOutOfRangeException e) {
    		return "Invalid value for 'day of month': " + e.getMessage();
    	}

    	String month = cronjob.getMonth();
    	try {
    		xpr = new NumberExpression(month);
    		xpr.fitsRange(1, 12);
    	} catch (ImproperCronValueException e) {
    		return "Invalid expression for 'month': " + e.getMessage();
    	} catch (CronValueOutOfRangeException e) {
    		return "Invalid value for 'month': " + e.getMessage();
    	}

    	String year = cronjob.getYear();
    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    	int thisYear = cal.get(Calendar.YEAR);
    	try {
    		xpr = new NumberExpression(year);
    		xpr.fitsRange(thisYear, thisYear + 3);
    	} catch (ImproperCronValueException e) {
    		return "Invalid expression for 'year': " + e.getMessage();
    	} catch (CronValueOutOfRangeException e) {
    		return "Invalid value for 'year': " + e.getMessage();
    	}

    	String dayOfWeek = cronjob.getDayOfWeek();
    	try {
    		xpr = new NumberExpression(dayOfWeek);
    		xpr.fitsRange(0, 7);
    	} catch (ImproperCronValueException e) {
    		return "Invalid expression for 'day of week': " + e.getMessage();
    	} catch (CronValueOutOfRangeException e) {
    		return "Invalid value for 'day of week': " + e.getMessage();
    	}

    	if (cronjob.getMaxRunMinutes() < 0 || cronjob.getMaxRunMinutes() > 1440)
    		return String.format("Invalid value for 'max runtime': Value %d exceeds range [%d - %d]", cronjob.getMaxRunMinutes(), 0, 1440);

    	return null;
	}

	public static Date calcNextRun(Cronjob j, Date now) throws ImproperCronValueException {

		if (!j.getDayOfMonth().equals("*") && !j.getDayOfWeek().equals("*"))
			throw new ImproperCronValueException("Cannot process entry with both, day-of-month and day-of-week expression");

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(now);

		// consider next minute
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.MINUTE, 1);

		try {

			calcNextYear(j, cal);

			calcNextMonth(j, cal);

			calcNextDayOfMonth(j, cal);

			calcNextDayOfWeek(j, cal);

			calcNextHour(j, cal);

			calcNextMinute(j, cal);
		}
		catch (CronValueMismatchException e) {
			LOG.log(Level.INFO, "Can't find next execution time for " + j, e);
			return null;
		}

		if (cal.getTime().before(now)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			df.setTimeZone(TimeZone.getTimeZone("GMT"));

			LOG.log(Level.INFO, "Determined a next-run time in the past: " + df.format(cal.getTime()));
			return null;
		}

		return cal.getTime();
	}

	private static void calcNextYear(Cronjob j, Calendar cal) throws ImproperCronValueException, CronValueMismatchException {
		int m = cal.get(Calendar.YEAR);
		NumberExpression xpr = new NumberExpression(j.getYear());
		if (!xpr.matchesAll()) {
			boolean match = false;
			for (int i = m; i < m + 10; i++) {
				if (xpr.matches(i)) {
					match = true;
					cal.set(Calendar.YEAR, i);
					if (i != m) {
						cal.set(Calendar.MONTH, 0);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
					}
					break;
				}
			}
			if (!match) {
				throw new CronValueMismatchException("Can't determine a year matching " + xpr);
			}
		}
	}

	private static void calcNextMonth(Cronjob j, Calendar cal) throws ImproperCronValueException, CronValueMismatchException {
		int m = cal.get(Calendar.MONTH);
		NumberExpression xpr = new NumberExpression(j.getMonth());
		if (!xpr.matchesAll()) {
			boolean through = false;
			boolean match = false;
			for (int i = m; i != m || !through; i++) {
				if (i >= 12) i = 0;
				if (xpr.matches(i+1)) {
					match = true;
					cal.set(Calendar.MONTH, i);
					if (i != m) {
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
					}
					break;
				}
				through = true;
			}
			if (!match) {
				throw new CronValueMismatchException("Can't determine a year matching " + xpr);
			}
		}
	}

	private static void calcNextDayOfMonth(Cronjob j, Calendar cal) throws ImproperCronValueException, CronValueMismatchException {
		int m = cal.get(Calendar.DAY_OF_MONTH);
		int x = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		NumberExpression xpr = new NumberExpression(j.getDayOfMonth());
		if (!xpr.matchesAll()) {
			boolean through = false;
			boolean match = false;
			for (int i = m; i != m || !through; i++) {
				if (i > x) {
					i = 1;
					cal.add(Calendar.MONTH, 1);
					calcNextMonth(j, cal);
				}
				if (xpr.matches(i)) {
					match = true;
					cal.set(Calendar.DAY_OF_MONTH, i);
					if (i != m) {
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
					}
					break;
				}
				through = true;
			}
			if (!match) {
				throw new CronValueMismatchException("Can't determine a year matching " + xpr);
			}
		}
	}

	private static void calcNextDayOfWeek(Cronjob j, Calendar cal) throws ImproperCronValueException, CronValueMismatchException {
		int m = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int m0 = cal.get(Calendar.MONTH);
		NumberExpression xpr = new NumberExpression(j.getDayOfWeek());
		if (!xpr.matchesAll()) {
			boolean through = false;
			boolean match = false;
			for (int i = m; i != m || !through; i++) {
				if (i > 7) {
					i = 0;
				}
				if (xpr.matches(i)) {
					match = true;
					int dd = (i + 7 - m) % 7;
					cal.add(Calendar.DAY_OF_YEAR, dd);
					int m1 = cal.get(Calendar.MONTH);
					if (m1 != m0) calcNextMonth(j, cal);
					if (i != m) {
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
					}
					break;
				}
				through = true;
			}
			if (!match) {
				throw new CronValueMismatchException("Can't determine a year matching " + xpr);
			}
		}
	}

	private static void calcNextHour(Cronjob j, Calendar cal) throws ImproperCronValueException, CronValueMismatchException {
		int m = cal.get(Calendar.HOUR_OF_DAY);
		NumberExpression xpr = new NumberExpression(j.getHour());
		if (!xpr.matchesAll()) {
			boolean through = false;
			boolean match = false;
			for (int i = m; i != m || !through; i++) {
				if (i >= 24) {
					i = 0;
					cal.add(Calendar.DAY_OF_MONTH, 1);
					calcNextDayOfMonth(j, cal);
				}
				if (xpr.matches(i)) {
					match = true;
					cal.set(Calendar.HOUR_OF_DAY, i);
					if (i != m) cal.set(Calendar.MINUTE, 0);
					break;
				}
				through = true;
			}
			if (!match) {
				throw new CronValueMismatchException("Can't determine a year matching " + xpr);
			}
		}
	}

	private static void calcNextMinute(Cronjob j, Calendar cal) throws ImproperCronValueException, CronValueMismatchException {
		int m = cal.get(Calendar.MINUTE);
		NumberExpression xpr = new NumberExpression(j.getMinute());
		if (!xpr.matchesAll()) {
			boolean through = false;
			boolean match = false;
			for (int i = m; i != m || !through; i++) {
				if (i >= 60) {
					i = 0;
					cal.add(Calendar.HOUR_OF_DAY, 1);
					calcNextHour(j, cal);
				}
				if (xpr.matches(i)) {
					match = true;
					cal.set(Calendar.MINUTE, i);
					break;
				}
				through = true;
			}
			if (!match) {
				throw new CronValueMismatchException("Can't determine a year matching " + xpr);
			}
		}
	}

}
