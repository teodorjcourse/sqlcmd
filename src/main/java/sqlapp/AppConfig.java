package sqlapp;

public class AppConfig {
	public static final String TEXT_FILE = "resources/texts.yml";
	public static final String CONFIGURATION_FILE = "resources/database.properties";

	public static TranslationBase translationBase;

	static {
		translationBase = new TranslationBase(new YAMLTexts(TEXT_FILE), Locale.RU);
	}

	private AppConfig() {}
}
