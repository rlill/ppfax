package farm.chaos.ppfax.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testGetIdFromUri() {
		Assert.assertEquals(4321L, StringUtils.getIdFromUri("/category/4321"));
	}

	@Test
	public void testGetArticleIdFromUri() {
		Assert.assertEquals(1234L, StringUtils.getArticleIdFromUri("/topic/topic/title-of-the-article-1234.html"));
	}

	@Test
	public void testGetRnoFromUri() {
		Assert.assertEquals(3, StringUtils.getRnoFromUri("/img/3/12341234"));
	}
}
