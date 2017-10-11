package sqlapp;

import java.util.List;

public class TranslationBase {
	private Locale locale;
	private YAMLTexts texts;

	public TranslationBase(YAMLTexts texts, Locale locale) {
		this.texts = texts;
		this.locale = locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale locale() {
		return locale;
	}

	public String getTranslation(String key) {
		return String.format(texts.textByKey(key, locale.toString()));
	}
}
