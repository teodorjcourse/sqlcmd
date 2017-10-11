package sqlapp.utils;

public class StringUtils {
	private static final String PATTERN_TENPLATE = "\\{%s\\}";

	/**
	 *  Searchs in passed text {@param text} substrings of type "{number}" and replaces them
	 *  with corresponding value with index = number from {@param rest} array
	 * @param text
	 * @param rest
	 * @return
	 */
	public static String substitute(String text, String ... rest) {
//		return String.format(text, rest);

		String result = text;

		for (int index = 0; index < rest.length; index++) {
			String regEx = String.format(PATTERN_TENPLATE, index);
			result = result.replaceAll(regEx, rest[index]);
		}

		return result;
	}

	public static boolean nullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

}
