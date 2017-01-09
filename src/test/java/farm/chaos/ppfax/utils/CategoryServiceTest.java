package farm.chaos.ppfax.utils;

import org.junit.Assert;
import org.junit.Test;

public class CategoryServiceTest {

	@Test
	public void testRightPath() {
		Assert.assertEquals("/c-and-a", CategoryService.rightPath("/blubb/C&A#"));
	}
}
