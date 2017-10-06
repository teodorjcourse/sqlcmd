package sqlapp;

public enum Locale {
	RU("ru"),
	EN("en");

	private String locale;

	Locale(String locale) {
		this.locale = locale;

	}

	@Override
	public String toString() {
		return locale;
	}
}
