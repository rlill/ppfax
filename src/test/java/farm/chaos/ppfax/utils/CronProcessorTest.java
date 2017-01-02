package farm.chaos.ppfax.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import farm.chaos.ppfax.model.Cronjob;

public class CronProcessorTest {

	private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	@Test
	public void testNextExecutionMinutes() throws Exception {

		Cronjob j = new Cronjob();
		j.setMinute("45");

		Date time = df.parse("2016-10-10 13-16-24");

		Date next = CronProcessor.calcNextRun(j, time);

		Assert.assertEquals("2016-10-10 13-45-00", df.format(next));

	}

	@Test
	public void testVerifyParameters() throws Exception {
		// TODO...
	}

	// TBC...

}
