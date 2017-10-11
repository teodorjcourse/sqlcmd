package sqlapp.utils;

import sqlapp.Log;

public class SplitedString {
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
