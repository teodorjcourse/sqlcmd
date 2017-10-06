package sqlapp.utils;

import sqlapp.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static String substitute(String text, String ... rest) {
//		return String.format(text, rest);


		Pattern p = Pattern.compile("\\{\\d*\\}");
		Matcher m = p.matcher(String.format(text));
		StringBuffer sb = new StringBuffer();

		int argIndex = 0;
		while (m.find()) {
			String replaceString = argIndex < rest.length ? rest[argIndex++] : "";

			m.appendReplacement(sb, replaceString);
		}

		m.appendTail(sb);


		return sb.toString();
	}

	public static boolean nullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static class SplitedString {
		private String source;
		private String pattern;
		private String[] result;


		private SplitedString(String source, String pattern) {
			this.source = source;
			this.pattern = pattern;
		}

		public String source() {
			return source;
		}

		public String[] all() {
			return result;
		}

		public String get(int index) {
			return index < result.length ? result[index] : "";
		}

		private void split() {
			try {
				result = source.replace(" ", "").split(pattern);
			} catch (Throwable any) {
				Log.err("Split error", (String.format("Source \"%s\", pattern \"%s\"", source, pattern)));
				result = new String[]{};
			}
		}

		public static SplitedString split(String source, String pattern) {
			SplitedString res = new SplitedString(source, pattern);
			res.split();


			return res;
		}
	}

}
