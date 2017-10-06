package sqlapp;

public class ApplicationConfig {
	private static final String TEXT_FILE = "texts.yml";

	private static TextBase textConfig;
	private static Locale locale;

	static {
		textConfig = new TextBase(TEXT_FILE);
		locale = Locale.RU;
	}

	private ApplicationConfig() {}

	public static Locale locale() {
		return locale;
	}

	public static class Translation {
		private String key;
		private String locale;

		public Translation(String key, String locale) {
			this.key = key;
			this.locale = locale;
		}

		public Translation(String key) {
			this(key, ApplicationConfig.locale.toString());
		}

		public String translation() {
			return translation(false);
		}

		public String translation(boolean formatted) {
			if (formatted) {
				return String.format(textConfig.translationByKey(key, locale));
			}

			return textConfig.translationByKey(key, locale);
		}
	}

	public static class FormattedTranslation {
		private String key;
		private String locale;

		public FormattedTranslation(String key, String locale) {
			this.key = key;
			this.locale = locale;
		}

		public FormattedTranslation(String key) {
			this(key, ApplicationConfig.locale.toString());
		}

		public String translation() {
			return String.format(textConfig.translationByKey(key, locale));
		}
	}
}
