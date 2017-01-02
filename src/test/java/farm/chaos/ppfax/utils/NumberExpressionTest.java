package farm.chaos.ppfax.utils;

import org.junit.Assert;
import org.junit.Test;

public class NumberExpressionTest {

	private NumberExpression xpr;

	@Test
	public void testMatchAll() throws ImproperCronValueException {

		xpr = new NumberExpression("*");
		Assert.assertTrue(xpr.matchesAll());

		xpr = new NumberExpression("0");
		Assert.assertFalse(xpr.matchesAll());
	}

	@Test
	public void testMatchPoint() throws ImproperCronValueException {

		xpr = new NumberExpression("*");
		for (int i = 0; i < 10; i++)
			Assert.assertTrue(xpr.matches(i));

		xpr = new NumberExpression("4");
		for (int i = 0; i < 10; i++)
			if (i == 4)
				Assert.assertTrue(xpr.matches(i));
			else
				Assert.assertFalse(xpr.matches(i));
	}

	@Test
	public void testMatchRange() throws ImproperCronValueException {

		xpr = new NumberExpression("4-6");
		for (int i = 0; i < 10; i++)
			if (i >= 4 && i <= 6)
				Assert.assertTrue(xpr.matches(i));
			else
				Assert.assertFalse(xpr.matches(i));
	}

	@Test
	public void testMatchInterval() throws ImproperCronValueException {

		xpr = new NumberExpression("3/5");
		for (int i = 0; i < 10; i++)
			if (i == 3 || i == 8)
				Assert.assertTrue(xpr.matches(i));
			else
				Assert.assertFalse(xpr.matches(i));
	}

	@Test
	public void testMatchMixed() throws ImproperCronValueException {

		xpr = new NumberExpression("2-4,6,8-20");
		for (int i = 0; i < 10; i++)
			if (i == 2 || i == 3 || i == 4 || i == 6 || i == 8 || i == 9)
				Assert.assertTrue(xpr.matches(i));
			else
				Assert.assertFalse(xpr.matches(i));
	}

}
