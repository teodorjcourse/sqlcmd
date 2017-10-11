package utils;

import junit.framework.TestCase;
import sqlapp.utils.StringUtils;

public class StringUtilsTest extends TestCase {

	public void testSubstitutes() {
		String[] replaceParams = {
				"val_1",
				"val_2",
				"val_3",
				"val_4",
				"val_5",
				"val_6",
				"val_7",
				"val_8",
				"val_9",
				"val_10"
		};

		String[] cases = {
				"{0}",
				"{1}",
				"{0}{1}",
				"{1}{0}",
				"{1}{0}{1}",
				"{1}{1}",
				"{0}{0}",
				"{20}"
		};

		String[] expectetions = {
				"val_1",
				"val_2",
				"val_1val_2",
				"val_2val_1",
				"val_2val_1val_2",
				"val_2val_2",
				"val_1val_1",
				"{20}"
		};

		for (int index = 0; index < cases.length; index++) {
			String result = StringUtils.substitute(cases[index], replaceParams);
			assertEquals(result, expectetions[index]);
		}
	}

	public void testNullOrEmpty() {
		String[] cases = {
				null,
				"",
				"    "
		};

		for (String string : cases) {
			assertTrue(StringUtils.nullOrEmpty(string));
		}
	}
}
