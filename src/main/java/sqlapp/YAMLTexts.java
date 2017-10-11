package sqlapp;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class YAMLTexts {
	private final String configFileName;

	private HashMap<String, HashMap<String, String>> config;

	public YAMLTexts(String configFileName) {
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
		} catch (Throwable any) {
			Log.err(any.getMessage());
		}
	}

	public String textByKey(String key, String locale) {
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

class YamlFile<T> {
	private String fileName;

	private T content;

	public YamlFile(String fileName) {
		this.fileName = fileName;
	}

	public T content()
			throws FileNotFoundException, IOException
	{
		if (content != null) {
			return content;
		}

		File f = new File(getClass().getResource(fileName).getFile());
		InputStream input = new FileInputStream(f);

		Yaml yaml = new Yaml();
		content = (T) yaml.load(input);

		input.close();

		return content;
	}


}

