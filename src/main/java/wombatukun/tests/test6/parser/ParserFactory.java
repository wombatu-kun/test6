package wombatukun.tests.test6.parser;

import wombatukun.tests.test6.exception.ParserException;

import java.io.InputStream;
import java.util.Properties;

public class ParserFactory {

	private static class SingletonHolder {
		private static final ParserFactory INSTANCE = new ParserFactory();
	}

	private Properties parsers;

	private ParserFactory() {
		try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("parsers.properties")) {
			parsers = new Properties();
			parsers.load(in);
		} catch (Exception e) {
			throw new ParserException("Unable to load parsers.properties");
		}
	}

	public static ParserFactory getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public OrderParser getParserByFileName(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if ((dotIndex == -1) || (dotIndex == filename.length()-1)) {
			throw new ParserException("File format not specified");
		}

		String extension = filename.substring(dotIndex+1).toUpperCase();
		String className = parsers.getProperty(extension);
		if (className == null) {
			throw new ParserException(extension + "-files are not supported");
		}

		OrderParser parser;
		try {
			parser = (OrderParser) Class.forName(className).getDeclaredConstructor(String.class).newInstance(filename);
		} catch (Exception e) {
			throw new ParserException("Unable to load " + extension + "-parser by name " + className);
		}
		return parser;
	}
}
