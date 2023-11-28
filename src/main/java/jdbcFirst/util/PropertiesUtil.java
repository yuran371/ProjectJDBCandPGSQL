package jdbcFirst.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 004 Properties файл
 */

public final class PropertiesUtil {

	private static final Properties PROPERTIES = new Properties();

	static {
		loadProperties();
	}

	private PropertiesUtil() {
	}

	public static String get(String key) {
		return PROPERTIES.getProperty(key);
	}

	/*
	 * getClass().getClassLoader().getResource() treats the path as an absolute path from the root of the classpath.
	 * getClass().getResource() treats the path as a relative path starting from the package of the class.
	 */
	private static void loadProperties() {
		try (InputStream inputStream = PropertiesUtil.class
				.getClassLoader()
				.getResourceAsStream("application.properties")) {
			PROPERTIES.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
