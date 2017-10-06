package sqlapp;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class YamlFile<T> {
	private String fileName;

	private T content;

	public YamlFile(String fileName) {
		this.fileName = fileName;
	}

	public T content() throws FileNotFoundException {
		if (content != null) {
			return content;
		}

		ClassLoader classLoader = getClass().getClassLoader();
		File f = new File(classLoader.getResource(fileName).getFile());

		InputStream input = null;

		Yaml yaml = new Yaml();

		input = new FileInputStream(f);

		content = (T) yaml.load(input);


		return content;
	}


}
