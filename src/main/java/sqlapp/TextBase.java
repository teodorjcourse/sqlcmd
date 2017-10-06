package sqlapp;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TextBase {
	private final String configFileName;

	private HashMap<String, HashMap<String, String>> config;

	public TextBase(String configFileName) {
		this.configFileName = configFileName;
		config = new HashMap<>();

		LinkedHashMap obj;

		try {
			obj = (LinkedHashMap) new YamlFile(this.configFileName).content();
			try {
				parse(obj);
			} catch (Throwable any) {
				Log.err(String.format("Parsing text file {%s} error", this.configFileName));
			}
		} catch (FileNotFoundException e) {
			Log.err(String.format("Text file {%s} load error:", this.configFileName));
		}
	}

	public String translationByKey(String key, String locale) {
		String result = key;

		try {
			result = config.get(locale).get(key);
		} catch (Throwable any) {
			Log.err("Localisation error", key);
		}

		if (result == null) {
			Log.warn(String.format("Translation for key {%s} doesn't exist", key));

			result = key;
		}

		return result;
	}

	private void parse(LinkedHashMap yamlConfig) {
		for (String key : (Set<String>) yamlConfig.keySet()) {
			Map<String, String> subValues = (Map<String, String>) yamlConfig.get(key);

			for (String locale : subValues.keySet()) {
				HashMap<String, String>  map = config.get(locale);

				if (map == null) {
					map = new HashMap<>();
					config.put(locale, map);
				}

				map.put(key, subValues.get(locale));
			}
		}
	}
}
