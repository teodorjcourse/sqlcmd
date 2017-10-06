package sqlapp;

public class Log {

	public static void err(String... errlog) {
		print("[ERROR]", errlog);
	}

	public static void warn(String... errlog) {
		print("[WARNING]", errlog);
	}

	private static void print(String errCode, String... errlog) {
		StringBuilder result = new StringBuilder();
		result.append(errCode).append(System.lineSeparator());

		for (String arg : errlog) {
			result.append("\t").append(arg).append(System.lineSeparator());
		}

		System.out.println(result);
	}

}
